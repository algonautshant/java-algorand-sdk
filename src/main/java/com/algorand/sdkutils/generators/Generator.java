package com.algorand.sdkutils.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


public class Generator {

	JsonNode root;

	static BufferedWriter getFileWriter(String className, String directory) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(directory + "/" + className + ".java")));
		return bw;
	}

	static String getCamelCase(String name, boolean firstCap) {
		boolean capNext = firstCap;
		char [] newName = new char[name.length()+1];
		int n = 0;
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '-') {
				capNext = true;
				continue;
			}
			if (capNext) {
				newName[n++] = Character.toUpperCase(name.charAt(i));
				capNext = false;
			} else {
				newName[n++] = name.charAt(i);
			}
		}
		return new String(newName, 0, n);
	}

	static String getTypeNameFromRef(String ref) {
		StringTokenizer st = new StringTokenizer(ref, "/");
		String ans = "";
		while (st.hasMoreTokens()) {
			ans = st.nextToken();
		}
		return ans;
	}

	static String getType(JsonNode prop, boolean asObject, Map<String, Set<String>> imports) {

		if (prop.get("$ref") != null) {
			JsonNode typeNode = prop.get("$ref");
			String type = getTypeNameFromRef(typeNode.asText());
			return type;
		}

		JsonNode typeNode = prop.get("type") != null ? prop : prop.get("schema");
		String type = typeNode.get("type").asText();
		String format = typeNode.get("x-algorand-format") != null ? typeNode.get("x-algorand-format").asText() : "";
		if (!format.isEmpty() ) {
			switch (format) {
			case "uint64":
				return "java.math.BigInteger";
			case "RFC3339 String":
				return "java.util.Date";
			case "Address":
				addImport(imports, "com.algorand.algosdk.crypto.Address"); 
				return "Address";
			case "SignedTransaction":
				addImport(imports, "com.algorand.algosdk.transaction.SignedTransaction");
				return "SignedTransaction";
			}
		}
		switch (type) {
		case "integer":
			return asObject ? "Long" : "long";
		case "object":
		case "string":
			return "String";
		case "boolean":
			return asObject ? "Boolean" : "boolean";			
		case "array":
			JsonNode arrayTypeNode = prop.get("items");
			String typeName = getType(arrayTypeNode, asObject, imports);
			return "List<" + typeName + ">";
		default:
			throw new RuntimeException("Unrecognized type: " + type);	
		}
	}

	static String getStringValueOfStatement(String propType, String propName) {
		switch (propType) {
		case "java.util.Date":
			return "new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'h:m:ssZ\").format(" + propName + ")";
		default:
			return "String.valueOf("+propName+")";
		}
	}

	static void addImport(Map<String, Set<String>> imports, String imp) {
		String key = imp.substring(0, imp.indexOf('.'));
		if (imports.get(key) == null) {
			imports.put(key, new TreeSet<String>());
		}
		imports.get(key).add(imp);
	}
	
	static String getImports(Map<String, Set<String>> imports) {
		StringBuffer sb = new StringBuffer();

		Set<String> java = imports.get("java");
		if (java != null) {
			for (String imp : java) {
				sb.append("import " + imp + ";\n");
			}
			if (imports.get("com") != null) {
				sb.append("\n");
			}
		}

		Set<String> com = imports.get("com");
		if (com != null) {
			for (String imp : com) {
				sb.append("import " + imp + ";\n");
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
	static String getPropertyWithJsonSetter(String typeObj, String javaName, String jprop){
		StringBuffer buffer = new StringBuffer();
		switch (typeObj) {
		case "java.util.DateTime":
			throw new RuntimeException("Parsing of time is not yet implemented!");
		case "String":
		case "Long":
		case "long":
		case "boolean":
		case "java.math.BigInteger":
		default: // List and Models with Json properties
			buffer.append("\t" + "@JsonProperty(\"" + jprop + "\")");
			buffer.append("\n\tpublic " + typeObj + " " + javaName);
			if (typeObj.contains("List<")) {
				buffer.append(" = new Array" + typeObj + "()");
			}
			buffer.append(";\n");
		}
		return buffer.toString();
	}


	static boolean needsClassImport(String type) {
		switch (type) {
		case "integer":
			return false;
		case "string":
			return false;
		default:	
			return true;
		}
	}

	static String formatComment(String comment, String tab, boolean full) {
		StringBuffer sb = new StringBuffer();

		comment = comment.replace("\\[", "(");
		comment = comment.replace("\\]", ")");
		comment = comment.replace("\n", " __NEWLINE__ ");

		if (full) {
			sb.append(tab+"/**");
			sb.append("\n"+tab+" * ");
		} else {
			sb.append(tab+" * ");			
		}
		
		StringTokenizer st = new StringTokenizer(comment);
		int line = 0;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.contentEquals("__NEWLINE__")) {
				if (line == 0) {
					continue;
				} else {
					line = 0;
					sb.append("\n"+tab+" * ");
					continue;
				}
			}
			if (line + token.length() > 80) {
				line = 0;
				sb.append("\n"+tab+" * ");
			} 
			token = token.replace('*', ' ');
			sb.append(token + " ");
			line += token.length() + 1;
		}
		if (full) {
			sb.append("\n"+tab+" */");
		}
		return sb.toString();
	}

	static Iterator<Entry<String, JsonNode>> getSortedProperties(JsonNode properties) {
		Iterator<Entry<String, JsonNode>> props = properties.fields();
		TreeMap<String, JsonNode> propMap = new TreeMap<String, JsonNode>();
		while (props.hasNext()) {
			Entry<String, JsonNode> e = props.next();
			propMap.put(e.getKey(), e.getValue());
		}
		Iterator<Entry<String, JsonNode>>sortedProps = propMap.entrySet().iterator();
		return sortedProps;
	}

	Iterator<Entry<String, JsonNode>> getSortedParameters(JsonNode properties) {
		TreeMap<String, JsonNode> tm = new TreeMap<String, JsonNode>();
		if (properties.isArray()) {
			ArrayNode jsonArrayNode = (ArrayNode) properties;
			for (int i = 0; i < jsonArrayNode.size(); i++) {
				JsonNode node = jsonArrayNode.get(i);
				JsonNode typeNode = null;
				if (node.get("$ref") != null) {
					typeNode = this.getFromRef(node.get("$ref").asText());
				} else {
					typeNode = node;
				}
				tm.put(typeNode.get("name").asText(), typeNode);
			}
			Iterator<Entry<String, JsonNode>>sortedParams = tm.entrySet().iterator();
			return sortedParams;
		}
		return null;
	}

	// Model class properties
	static void writeProperties(StringBuffer buffer, Iterator<Entry<String, JsonNode>> properties, Map<String, Set<String>> imports) {
		while (properties.hasNext()) {
			Entry<String, JsonNode> prop = properties.next();
			String jprop = prop.getKey();
			String javaName = getCamelCase(jprop, false);
			String typeObj = getType(prop.getValue(), true, imports);
			if (typeObj.contains("List<")) {
				addImport(imports, "java.util.ArrayList");
				addImport(imports, "java.util.List");
			}

			String desc = null;
			if (prop.getValue().get("description") != null) {
				desc = prop.getValue().get("description").asText();
				desc = formatComment(desc, "\t", true);
			}

			// public type
			if (desc != null) buffer.append(desc);
			buffer.append(getPropertyWithJsonSetter(typeObj, javaName, jprop));
			buffer.append("\n");
		}
	}

	static void writeCompareMethod(String className, StringBuffer buffer, Iterator<Entry<String, JsonNode>> properties) {
		buffer.append("	@Override\n" + 
				"	public boolean equals(Object o) {\n" + 
				"\n" + 
				"		if (this == o) return true;\n" + 
				"		if (o == null) return false;\n");
		buffer.append("\n"); 
		buffer.append("		" + className + " other = (" + className + ") o;\n");
		while (properties.hasNext()) {
			Entry<String, JsonNode> prop = properties.next();
			String jprop = prop.getKey();
			String javaName = getCamelCase(jprop, false);
			buffer.append("		if (!Objects.deepEquals(this." + javaName + ", other." + javaName + ")) return false;\n");
		}
		buffer.append("\n		return true;\n	}\n");
	}

	static void writeClass(String className, JsonNode propertiesNode, String desc, String directory, String pkg) throws IOException {
		System.out.println("Generating ... " + className);

		Iterator<Entry<String, JsonNode>> properties = getSortedProperties(propertiesNode);
		className = Generator.getCamelCase(className, true);
		BufferedWriter bw = getFileWriter(className, directory);
		bw.append("package " + pkg + ";\n\n");

		HashMap<String, Set<String>> imports = new HashMap<String, Set<String>>();
		StringBuffer body = new StringBuffer();

		writeProperties(body, properties, imports);
		
		properties = getSortedProperties(propertiesNode);
		writeCompareMethod(className, body, properties);

		addImport(imports, "java.util.Objects"); // used by Objects.deepEquals

		addImport(imports, "com.algorand.algosdk.v2.client.common.PathResponse");
		addImport(imports, "com.fasterxml.jackson.annotation.JsonProperty");

		bw.append(getImports(imports));
		if (desc != null) {
			bw.append(desc);
			bw.append("\n");
		}
		bw.append("public class " + className + " extends PathResponse {\n\n");
		bw.append(body);
		bw.append("}\n");
		bw.close();
	}

	static ArrayList<String> getPathInserts(String path) {
		ArrayList<String> nPath = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(path, "/");
		while (st.hasMoreTokens()) {
			String elt = st.nextToken();
			if (elt.charAt(0) == '{') {
				String jName = Generator.getCamelCase(elt.substring(1, elt.length()-1), false);
				nPath.add(jName);
			} else {
				nPath.add("\"" + elt + "\"");
			}
		}
		return nPath;
	}

	static String getQueryResponseMethod(String returnType, String getOrPost) {
		String ret = 
				"	@Override\n" + 
						"	public Response<" + returnType + "> execute() throws Exception {\n" + 
						"		Response<" + returnType + "> resp = baseExecute();\n" + 
						"		resp.setValueType(" + returnType + ".class);\n" + 
						"		return resp;\n" + 
						"	}\n\n";
		return ret;
	}

	static boolean isRequired(JsonNode prop) {
		if (prop.get("required") != null) {
			return prop.get("required").asBoolean();
		}
		return false;
	}

	static boolean inPath(JsonNode prop) {
		if (prop.get("in") != null) {
			return prop.get("in").asText().compareTo("path") == 0;
		}
		return false;
	}

	static String processQueryParams(
			StringBuffer generatedPathsEntry,
			Iterator<Entry<String, JsonNode>> properties,
			String className, 			
			String path,
			String returnType,
			String getOrPost,
			Map<String, Set<String>> imports) {

		StringBuffer decls = new StringBuffer();
		StringBuffer builders = new StringBuffer();
		StringBuffer constructorHeader = new StringBuffer();
		StringBuffer constructorBody = new StringBuffer();
		StringBuffer requestMethod = new StringBuffer();
		ArrayList<String> constructorComments = new ArrayList<String>();
		
		StringBuffer generatedPathsEntryBody = new StringBuffer();

		requestMethod.append(Generator.getQueryResponseMethod(returnType, getOrPost));
		requestMethod.append("	protected QueryData getRequestString() {\n");
		boolean pAdded = false;

		while (properties != null && properties.hasNext()) {
			Entry<String, JsonNode> prop = properties.next();
			String propName = Generator.getCamelCase(prop.getKey(), false);
			String setterName = Generator.getCamelCase(prop.getKey(), false);
			String propType = getType(prop.getValue(), true, imports);
			String propCode = prop.getKey();

			if (inPath(prop.getValue())) {
				decls.append("\tprivate " + propType + " " + propName + ";\n");
				if (prop.getValue().get("description") != null) {
					String desc = prop.getValue().get("description").asText();
					desc = formatComment("@param " + propName + " " + desc, "\t", false);
					constructorComments.add(desc);
				}

				constructorHeader.append(", " + propType + " " + propName);
				constructorBody.append("		this." + propName + " = " + propName + ";\n");
				
				if (pAdded) {
					generatedPathsEntry.append(",\n			");
				}
				generatedPathsEntry.append(propType + " " + propName);
				generatedPathsEntryBody.append(", " + propName);
				pAdded = true;
				continue;
			}

			if (prop.getValue().get("description") != null) {
				String desc = prop.getValue().get("description").asText();
				desc = formatComment(desc, "\t", true);
				builders.append(desc + "\n");
			}
			builders.append("\tpublic " + className + " " + setterName + "(" + propType + " " + propName + ") {\n");
			String valueOfString = getStringValueOfStatement(propType, propName);
			builders.append("\t\taddQuery(\"" + propCode + "\", "+ valueOfString +");\n");
			builders.append("\t\treturn this;\n");
			builders.append("\t}\n\n");

			if (isRequired(prop.getValue())) {
				requestMethod.append("		if (!qd.queries.containsKey(\"" + propName + "\")) {\n");
				requestMethod.append("			throw new RuntimeException(\"" +
						propCode + " is not set. It is a required parameter.\");\n		}\n");
			}

		}
		
		generatedPathsEntry.append(") {\n");
		generatedPathsEntry.append("		return new "+className+"((Client) this");
		generatedPathsEntry.append(generatedPathsEntryBody);
		generatedPathsEntry.append(");\n	}\n");

		// Add the path construction code. 
		// The path is constructed in the end, while the query params are added as the 
		ArrayList<String> al = getPathInserts(path);
		for (String str : al) {
			requestMethod.append(
					"		addPathSegment(String.valueOf(" + str + "));\n");
		}

		requestMethod.append("\n" + 
				"		return qd;\n" + 
				"	}");

		StringBuffer ans = new StringBuffer();
		ans.append(decls);
		if (!decls.toString().isEmpty()) {
			ans.append("\n");
		}

		// constructor
		if (constructorComments.size() > 0) {
			ans.append("	/**");
			for (String elt : constructorComments) {
				ans.append("\n" + elt);
			}
			ans.append("\n	 */\n");
		}
		ans.append("	public "+className+"(Client client");
		ans.append(constructorHeader);
		ans.append(") {\n		super(client, \""+getOrPost+"\");\n");
		ans.append(constructorBody);
		ans.append("	}\n\n");

		ans.append(builders);
		ans.append(requestMethod);
		return ans.toString();
	}

	JsonNode getFromRef(String ref) {
		StringTokenizer st = new StringTokenizer(ref, "/");
		JsonNode ans = root;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.charAt(0) == '#') {
				continue;
			}
			ans = ans.get(token);
		}
		return ans;
	}

	void writeQueryClass(
			StringBuffer generatedPathsEntry,
			JsonNode spec,
			String path,
			String directory,
			String pkg,
			String modelPkg) throws IOException { 

		String getOrPost;
		if (spec.get("post") != null) {
			spec = spec.get("post");
			getOrPost = "post";
		} else {
			spec = spec.get("get");
			getOrPost = "get";
		}

		String className = spec.get("operationId").asText();
		String methodName = Generator.getCamelCase(className, false);
		className = Generator.getCamelCase(className, true);
		
		generatedPathsEntry.append("\n	public " + className + " " + methodName + "(");
		
		JsonNode paramNode = spec.get("parameters");
		String returnType = "String";
		if (spec.get("responses").get("200").get("$ref") != null) { 
			returnType = spec.get("responses").get("200").get("$ref").asText();
			JsonNode returnTypeNode = this.getFromRef(returnType);
			if (returnTypeNode.get("schema").get("$ref") != null) {
				returnType = Generator.getTypeNameFromRef(returnTypeNode.get("schema").get("$ref").asText());
			} else if (returnTypeNode.get("schema").get("properties").size() == 1) {
				returnType = returnTypeNode.get("schema").get("properties").findValue("type").asText();
				returnType = Generator.getCamelCase(returnType, true);
			} else {
				returnType = Generator.getTypeNameFromRef(returnType);
				returnType = Generator.getCamelCase(returnType, true);
			}
		}
		String desc = spec.get("description") != null ? 
				spec.get("description").asText() : "";
				desc = desc + "\n" + path;
				System.out.println("Generating ... " + className);
				Iterator<Entry<String, JsonNode>> properties = null;
				if ( paramNode != null) {
					properties = getSortedParameters(paramNode);
				}

				BufferedWriter bw = getFileWriter(className, directory);
				bw.append("package " + pkg + ";\n\n");

				Map<String, Set<String>> imports = new HashMap<String, Set<String>>();
				addImport(imports, "com.algorand.algosdk.v2.client.common.Client");
				addImport(imports, "com.algorand.algosdk.v2.client.common.Query"); 
				addImport(imports, "com.algorand.algosdk.v2.client.common.QueryData");
				addImport(imports, "com.algorand.algosdk.v2.client.common.Response");
				if (needsClassImport(returnType.toLowerCase())) {
					addImport(imports, modelPkg + "." + returnType);
				}

				StringBuffer sb = new StringBuffer();
				sb.append("\n");
				sb.append(Generator.formatComment(desc, "", true));
				sb.append("\npublic class " + className + " extends Query {\n\n");
				sb.append(
						processQueryParams(
								generatedPathsEntry,
								properties, 
								className, 
								path,
								returnType, 
								getOrPost, 
								imports));
				sb.append("\n}");
				bw.append(getImports(imports));
				bw.append(sb);
				bw.close();
	}

	public static void generateAlgodIndexerObjects (JsonNode root, String rootPath, String pkg) throws IOException {

		JsonNode schemas = root.get("components") != null ? 
				root.get("components").get("schemas") : 
					root.get("definitions");
				Iterator<Entry<String, JsonNode>> classes = schemas.fields();
				while (classes.hasNext()) {
					Entry<String, JsonNode> cls = classes.next();
					String desc = null;
					if (cls.getValue().get("description") != null) {
						desc = cls.getValue().get("description").asText();
						desc = formatComment(desc, "", true);
					}
					writeClass(cls.getKey(), cls.getValue().get("properties"), desc, rootPath, pkg);
				}
	}

	public static void generateReturnTypes (JsonNode root, String rootPath, String pkg) throws IOException {
		JsonNode returns = root.get("components") != null ? 
				root.get("components").get("responses") : 
					root.get("responses");
				Iterator<Entry<String, JsonNode>> returnTypes = returns.fields();
				while (returnTypes.hasNext()) {
					Entry<String, JsonNode> rtype = returnTypes.next();
					System.out.println("looking at: " + rtype.getKey());
					JsonNode rSchema = rtype.getValue().get("content") != null ? 
							rtype.getValue().get("content").get("application/json").get("schema") : 
								rtype.getValue().get("schema"); 

							if (rSchema.get("$ref") != null ) {
								// It refers to a defined class
								continue;
							}
							if (rSchema.get("properties").size() == 1) {
								continue;
							}
							writeClass(rtype.getKey(), rSchema.get("properties"), null, rootPath, pkg);
				}
	}

	public void generateQueryMethods(String rootPath, String pkg, String modelPkg, String gpImpDirFile, String gpMethodsDirFile) throws IOException {
		// GeneratedPaths file
		BufferedWriter gpImports = new BufferedWriter(new FileWriter(new File(gpImpDirFile)));
		BufferedWriter gpMethods = new BufferedWriter(new FileWriter(new File(gpMethodsDirFile)));
		
		StringBuffer gpBody = new StringBuffer();

		JsonNode paths = this.root.get("paths");
		Iterator<Entry<String, JsonNode>> pathIter = paths.fields();
		while (pathIter.hasNext()) {
			Entry<String, JsonNode> path = pathIter.next();
			writeQueryClass(gpBody, path.getValue(), path.getKey(), rootPath, pkg, modelPkg);

			// Fill GeneratedPaths class
			String className = getCamelCase(path.getValue().findPath("operationId").asText(), true);
			gpImports.append("import " + pkg + "." + className + ";\n");
		}
		gpMethods.append(gpBody);
		gpMethods.close();
		gpImports.close();
	}

	public Generator (JsonNode root) {
		this.root = root;
	}
}
