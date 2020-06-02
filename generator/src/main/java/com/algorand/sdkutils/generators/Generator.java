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

// TypeDef hold together information about a type
// typeName is the generated code type: e.g. List<abc>, or MyEnumClassName
// def is the definition of the type. e.g. the class declaration of the enum class
// type is a loosely defined tag used by generator e.g. enum, list, etc.  
// e.g. For enum type, typeName will be enum class name, def will be the enum 
class TypeDef {
    public TypeDef(String typeName, String def, String type) {
        this.typeName = typeName;
        this.def = def;
        this.type = type;
    }
    public TypeDef(String typeName) {
        this.typeName = typeName;
        this.def = null;
        this.type = null;
    }
    public boolean isOfType(String type) {
        if (this.type == null) {
            return false;
        }
        return this.type.contentEquals(type);
    }

    @Override
    public String toString() {
        throw new RuntimeException("Should not get the string value of the object directly!");
    }
    public String typeName;
    public String def;

    private String type;
}

public class Generator {
    public static final String TAB = "    ";

    protected JsonNode root;

    static BufferedWriter getFileWriter(String className, String directory) throws IOException {
        File f = new File(directory + "/" + className + ".java");
        f.getParentFile().mkdirs();
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        return bw;
    }

    public static String getCamelCase(String name, boolean firstCap) {
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

    // Get TypeDef for Address type. 
    // It provides the special getter/setter needed for this type.
    static TypeDef getAddress(String propName, Map<String, Set<String>> imports, boolean forModel) {
        addImport(imports, "com.algorand.algosdk.crypto.Address");
        if (forModel) {
            addImport(imports, "java.security.NoSuchAlgorithmException");
        }

        StringBuffer sb = new StringBuffer();
        String javaName = getCamelCase(propName, false);

        sb.append(TAB + "@JsonProperty(\"" + propName + "\")\n" + 
                "    public void " + javaName + "(String "+ javaName +") throws NoSuchAlgorithmException {\n" + 
                "        this."+ javaName +" = new Address("+ javaName +");\n" + 
                "    }\n" + 
                "    @JsonProperty(\""+ propName +"\")\n" + 
                "    public String "+ javaName +"() throws NoSuchAlgorithmException {\n" + 
                "        return this."+ javaName +".encodeAsString();\n" + 
                "    }\n" + 
                "    public Address " + javaName + ";\n");
        return new TypeDef("Address", sb.toString(), "getterSetter");
    }

    // Get base64 encoded byte[] type.
    // It provides the special getter/setter needed for this type
    static TypeDef getBase64Encoded(String propName, Map<String, Set<String>> imports, boolean forModel) {
        if (imports != null) {
            addImport(imports, "com.algorand.algosdk.util.Encoder");
        }
        String javaName = getCamelCase(propName, false);
        StringBuffer sb = new StringBuffer();
        sb.append("    @JsonProperty(\"" + propName + "\")\n" +
                "    public void " + javaName + "(String base64Encoded) {\n" +
                "        this."+ javaName +" = Encoder.decodeFromBase64(base64Encoded);\n" +
                "    }\n" +
                "    @JsonProperty(\""+ propName +"\")\n" +
                "    public String "+ javaName +"() {\n" +
                "        return Encoder.encodeToBase64(this."+ javaName +");\n" +
                "    }\n" +
                "    public byte[] "+ javaName +";\n");
        // getterSetter typeName is only used in path.
        return new TypeDef("byte[]", sb.toString(), "getterSetter");
    }

    // Get array type of base64 encoded bytes.
    // It provides the special getter/setter needed for this type
    static TypeDef getBase64EncodedArray(String propName, Map<String, Set<String>> imports, boolean forModel) {
        if (forModel == false) {
            throw new RuntimeException("array of byte[] cannot yet be used in a path or path query.");
        }
        addImport(imports, "com.algorand.algosdk.util.Encoder");
        addImport(imports, "java.util.ArrayList");
        addImport(imports, "java.util.List");

        String javaName = getCamelCase(propName, false);
        StringBuffer sb = new StringBuffer();

        sb.append("    @JsonProperty(\"" + propName + "\")\n" +
                "    public void " + javaName + "(List<String> base64Encoded) {\n" +
                "         this." + javaName + " = new ArrayList<byte[]>();\n" +
                "         for (String val : base64Encoded) {\n" +
                "             this." + javaName + ".add(Encoder.decodeFromBase64(val));\n" +
                "         }\n" +
                "     }\n" +
                "     @JsonProperty(\"" + propName + "\")\n" +
                "     public List<String> " + javaName + "() {\n" +
                "         ArrayList<String> ret = new ArrayList<String>();\n" +
                "         for (byte[] val : this." + javaName + ") {\n" +
                "             ret.add(Encoder.encodeToBase64(val));\n" +
                "         }\n" +
                "         return ret; \n" +
                "     }\n" +
                "    public List<byte[]> " + javaName + ";\n");
        // getterSetter typeName is only used in path.
        return new TypeDef("", sb.toString(), "getterSetter");
    }

    // Get array type of base64 encoded bytes.
    // It provides the special getter/setter needed for this type
    static TypeDef getEnum(JsonNode prop, String propName) {
        JsonNode enumNode = prop.get("enum");
        if (enumNode == null) {
            throw new RuntimeException("Cannot find enum info in node: " + prop.toString());
        }
        StringBuffer sb = new StringBuffer();
        String enumClassName = getCamelCase(propName, true);
        sb.append(TAB + "public enum " + enumClassName + " {\n");

        Iterator<JsonNode> elmts = enumNode.elements();
        while(elmts.hasNext()) {
            String val = elmts.next().asText();
            sb.append(TAB + TAB + "@JsonProperty(\"" + val + "\") ");
            String javaEnum = getCamelCase(val, true).toUpperCase();
            sb.append(javaEnum);
            sb.append("(\"" + val + "\")");
            if (elmts.hasNext()) {
                sb.append(",\n");
            } else {
                sb.append(";\n\n");
            }
        }
        sb.append(TAB + TAB + "final String serializedName;\n");
        sb.append(TAB + TAB + "" + enumClassName + "(String name) {\n");
        sb.append(TAB + TAB + TAB + "this.serializedName = name;\n");
        sb.append(TAB + TAB + "}\n\n");
        sb.append(TAB + TAB + "@Override\n");
        sb.append(TAB + TAB + "public String toString() {\n");
        sb.append(TAB + TAB + TAB + "return this.serializedName;\n");
        sb.append(TAB + TAB + "}\n");

        sb.append(TAB + "}\n");
        enumClassName = "Enums." + enumClassName;
        return new TypeDef(enumClassName, sb.toString(), "enum");
    }

    // getType returns the type fron the JsonNode
    TypeDef getType(
            JsonNode prop, 
            boolean asObject,
            Map<String, Set<String>> imports,
            String propName, boolean forModel) {

        if (prop.get("$ref") != null) {
            JsonNode typeNode = prop.get("$ref");
            String type = getTypeNameFromRef(typeNode.asText());
            // Need to check here if this type does not have a class of its own 
            // No C/C++ style typedef in java, and this type could be a class with no properties
            prop = getFromRef(typeNode.asText());
            if (hasProperties(prop)) {
                return new TypeDef(type);
            }
        }

        if (prop.get("enum") != null) {
            if (!forModel && !propName.equals("format")) {
                addImport(imports, "com.algorand.algosdk.v2.client.model.Enums");
            }
            return getEnum(prop, propName);
        }

        JsonNode typeNode = prop.get("type") != null ? prop : prop.get("schema");
        String type = typeNode.get("type").asText();
        String format = getTypeFormat(typeNode, propName);
        if (!format.isEmpty() ) {
            switch (format) {
            case "uint64":
                return new TypeDef("java.math.BigInteger");
            case "RFC3339 String":
                addImport(imports, "java.util.Date");
                addImport(imports, "com.algorand.algosdk.v2.client.common.Utils");
                return new TypeDef("Date");
            case "Address":
                return getAddress(propName, imports, forModel);
            case "SignedTransaction":
                addImport(imports, "com.algorand.algosdk.transaction.SignedTransaction");
                return new TypeDef("SignedTransaction");
            case "binary":
                return getBase64Encoded(propName, null, forModel);
            case "byte":
            case "base64":
            case "digest":
                if (type.contentEquals("array")) {
                    return getBase64EncodedArray(propName, imports, forModel);
                } else {
                    return getBase64Encoded(propName, imports, forModel);
                }
            case "AccountID":
                break;
            case "BlockCertificate":
            case "BlockHeader":
                addImport(imports, "java.util.HashMap");
                return new TypeDef("HashMap<String,Object>");
            }
        }
        switch (type) {
        case "integer":
            return asObject ? new TypeDef("Long") : new TypeDef("long");
        case "object":
        case "string":
            return new TypeDef("String");
        case "boolean":
            return asObject ? new TypeDef("Boolean") : new TypeDef("boolean");
        case "array":
            JsonNode arrayTypeNode = prop.get("items");
            TypeDef typeName = getType(arrayTypeNode, asObject, imports, propName, forModel);
            return new TypeDef("List<" + typeName.typeName + ">", typeName.def, "list");
        default:
            throw new RuntimeException("Unrecognized type: " + type);
        }
    }

    // getTypeFormat returns the additional type formatting information
    // There could be multiple such tags in the spec file. This method knows which 
    // one is relevant here. 
    public static String getTypeFormat(JsonNode typeNode, String propName) {
        String format = typeNode.get("x-algorand-format") != null ? typeNode.get("x-algorand-format").asText() : "";
        String type = typeNode.get("type").asText();
        format = typeNode.get("format") != null && format.isEmpty() ? typeNode.get("format").asText() : format;
        format = typeNode.get("x-go-name") != null && format.isEmpty() ? typeNode.get("x-go-name").asText() : format;
        if ((propName.equals("address") || propName.contentEquals("accountId")) &&
                type.equals("string")) {
            format = "Address";
        }
        if (format.equals("base64")) {
            format = "byte";
        }
        return format;
    }

    static String getStringValueOfStatement(String propType, String propName) {
        switch (propType) {
        case "Date":
            return "Utils.getDateString(" + propName + ")";
        case "byte[]":
            return "Encoder.encodeToBase64(" + propName + ")";
        default:
            return "String.valueOf("+propName+")";
        }
    }

    // Imports are collected and organized before printed as import statements.
    // addImports adds a needed import class. 
    static void addImport(Map<String, Set<String>> imports, String imp) {
        String key = imp.substring(0, imp.indexOf('.'));
        if (imports.get(key) == null) {
            imports.put(key, new TreeSet<String>());
        }
        imports.get(key).add(imp);
    }

    // getImports organizes the imports and returns the block of import statements
    // The statements are unique, and organized. 
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

    // getPropertyWithJsonSetter formats the property into java declaration type with 
    // the appropriate json annotation.
    static String getPropertyWithJsonSetter(TypeDef typeObj, String javaName, String jprop){
        StringBuffer buffer = new StringBuffer();

        if (typeObj.isOfType("getterSetter")) {
            return typeObj.def.toString();
        }

        switch (typeObj.typeName) {
        case "java.util.DateTime":
            throw new RuntimeException("Parsing of time is not yet implemented!");
        case "String":
        case "Long":
        case "long":
        case "boolean":
        case "java.math.BigInteger":
        default: // List and Models with Json properties
            buffer.append(TAB + "@JsonProperty(\"" + jprop + "\")\n");
            buffer.append(TAB + "public " + typeObj.typeName + " " + javaName);
            if (typeObj.isOfType("list")) {
                buffer.append(" = new Array" + typeObj.typeName + "()");
            }
            buffer.append(";\n");
        }
        return buffer.toString();
    }

    // returns true if the type needs an import statement. 
    // Not needed for primitive types. 
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

    // formatComment formats the comment by breaking lines, and incorporating 
    // embedded formatting inside the comment. 
    // If the comment is embedded inside another comment, full == false
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
            sb.append("\n"+tab+" */\n");
        }
        return sb.toString();
    }

    // Returns an iterator in sorted order of the properties (json nodes). 
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

    // Returns an iterator in sorted order of the parameters (json nodes). 
    Iterator<Entry<String, JsonNode>> getSortedParameters(JsonNode properties) {
        TreeMap<String, JsonNode> tm = new TreeMap<String, JsonNode>();
        if (properties == null) {
            return tm.entrySet().iterator();
        }

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

    // Write the properties of the Model class.
    void writeProperties(StringBuffer buffer, Iterator<Entry<String, JsonNode>> properties, Map<String, Set<String>> imports) {
        while (properties.hasNext()) {
            Entry<String, JsonNode> prop = properties.next();
            String jprop = prop.getKey();
            String javaName = getCamelCase(jprop, false);
            TypeDef typeObj = getType(prop.getValue(), true, imports, jprop, true);
            if (typeObj.isOfType("list")) {
                addImport(imports, "java.util.ArrayList");
                addImport(imports, "java.util.List");
            }

            String desc = null;
            if (prop.getValue().get("description") != null) {
                desc = prop.getValue().get("description").asText();
                desc = formatComment(desc, TAB + "", true);
            }

            // public type
            if (desc != null) buffer.append(desc);
            buffer.append(getPropertyWithJsonSetter(typeObj, javaName, jprop));
            buffer.append("\n");
        }
    }

    // Writes the compare methods by adding a comparator for each class member. 
    static void writeCompareMethod(String className, StringBuffer buffer, Iterator<Entry<String, JsonNode>> properties) {
        buffer.append("    @Override\n" +
                "    public boolean equals(Object o) {\n" +
                "\n" +
                "        if (this == o) return true;\n" +
                "        if (o == null) return false;\n");
        buffer.append("\n");
        buffer.append("        " + className + " other = (" + className + ") o;\n");
        while (properties.hasNext()) {
            Entry<String, JsonNode> prop = properties.next();
            String jprop = prop.getKey();
            String javaName = getCamelCase(jprop, false);
            buffer.append("        if (!Objects.deepEquals(this." + javaName + ", other." + javaName + ")) return false;\n");
        }
        buffer.append("\n        return true;\n    }\n");
    }

    // writeClass writes the Model class. 
    // This is the root method for writing the complete class. 
    void writeClass(String className, JsonNode propertiesNode, String desc, String directory, String pkg) throws IOException {
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
        }
        bw.append("public class " + className + " extends PathResponse {\n\n");
        bw.append(body);
        bw.append("}\n");
        bw.close();
    }

    // getPathInserts converts the path string into individual tokens which correspond
    // to the class members in the generated code. 
    // These are used to set the path segments in the constructor. 
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

    static String getQueryResponseMethod(String returnType) {
        String ret =
                "    @Override\n" +
                        "    public Response<" + returnType + "> execute() throws Exception {\n" +
                        "        Response<" + returnType + "> resp = baseExecute();\n" +
                        "        resp.setValueType(" + returnType + ".class);\n" +
                        "        return resp;\n" +
                        "    }\n\n";
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
    static boolean inBody(JsonNode prop) {
        if (prop.get("in") != null) {
            return prop.get("in").asText().compareTo("body") == 0;
        }
        return false;
    }
    static boolean hasProperties(JsonNode itemNode) {
        if (itemNode.get("properties") == null) {
            return false;
        }
        return true;
    }

    // Query parameters need be in builder methods.
    // processQueryParameters do all the processing of the parameters. 
    String processQueryParams(
            StringBuffer generatedPathsEntry,
            Iterator<Entry<String, JsonNode>> properties,
            String className,
            String path,
            String returnType,
            String httpMethod,
            Map<String, Set<String>> imports) {

        StringBuffer decls = new StringBuffer();
        StringBuffer builders = new StringBuffer();
        StringBuffer constructorHeader = new StringBuffer();
        StringBuffer constructorBody = new StringBuffer();
        StringBuffer requestMethod = new StringBuffer();
        ArrayList<String> constructorComments = new ArrayList<String>();

        StringBuffer generatedPathsEntryBody = new StringBuffer();

        requestMethod.append(Generator.getQueryResponseMethod(returnType));
        requestMethod.append("    protected QueryData getRequestString() {\n");
        boolean pAdded = false;
        boolean addFormatMsgpack = false;

        while (properties != null && properties.hasNext()) {
            Entry<String, JsonNode> prop = properties.next();
            String propName = Generator.getCamelCase(prop.getKey(), false);
            String setterName = Generator.getCamelCase(prop.getKey(), false);
            TypeDef propType = getType(prop.getValue(), true, imports, propName, false);

            // Do not expose format property
            if (propType.typeName.equals("Enums.Format")) {
                addFormatMsgpack = true;
                continue;
            }
            String propCode = prop.getKey();

            // The parameters are either in the path or in the query

            // Populate generator structures for the in path parameters
            if (inPath(prop.getValue())) {
                if (propType.isOfType("enum")) {
                    throw new RuntimeException("Enum in paths is not supported! " + propName);
                }
                decls.append(TAB + "private " + propType.typeName + " " + propName + ";\n");
                if (prop.getValue().get("description") != null) {
                    String desc = prop.getValue().get("description").asText();
                    desc = formatComment("@param " + propName + " " + desc, TAB, false);
                    constructorComments.add(desc);
                }

                constructorHeader.append(", " + propType.typeName + " " + propName);
                constructorBody.append("        this." + propName + " = " + propName + ";\n");

                if (pAdded) {
                    generatedPathsEntry.append(",\n            ");
                }
                generatedPathsEntry.append(propType.typeName + " " + propName);
                generatedPathsEntryBody.append(", " + propName);
                pAdded = true;
                continue;
            }

            if (prop.getValue().get("description") != null) {
                String desc = prop.getValue().get("description").asText();
                desc = formatComment(desc, TAB, true);
                builders.append(desc);
            }
            builders.append(TAB + "public " + className + " " + setterName + "(" + propType.typeName + " " + propName + ") {\n");
            String valueOfString = getStringValueOfStatement(propType.typeName, propName);

            if (inBody(prop.getValue())) {
                builders.append(TAB + TAB + "addToBody("+ propName +");\n");
            } else {
                builders.append(TAB + TAB + "addQuery(\"" + propCode + "\", "+ valueOfString +");\n");
            }
            builders.append(TAB + TAB + "return this;\n");
            builders.append(TAB + "}\n");
            builders.append("\n");

            if (isRequired(prop.getValue())) {
                if (inBody(prop.getValue())) {
                    requestMethod.append("        if (qd.bodySegments.isEmpty()) {\n");
                } else {
                    requestMethod.append("        if (!qd.queries.containsKey(\"" + propName + "\")) {\n");
                }
                requestMethod.append("            throw new RuntimeException(\"" +
                        propCode + " is not set. It is a required parameter.\");\n        }\n");
            }

        }

        generatedPathsEntry.append(") {\n");
        generatedPathsEntry.append("        return new "+className+"((Client) this");
        generatedPathsEntry.append(generatedPathsEntryBody);
        generatedPathsEntry.append(");\n    }\n\n");

        // Add the path construction code.
        // The path is constructed in the end, while the query params are added as the
        ArrayList<String> al = getPathInserts(path);
        for (String str : al) {
            requestMethod.append(
                    "        addPathSegment(String.valueOf(" + str + "));\n");
        }

        requestMethod.append("\n" +
                "        return qd;\n" +
                "    }");

        StringBuffer ans = new StringBuffer();
        ans.append(decls);
        if (!decls.toString().isEmpty()) {
            ans.append("\n");
        }

        // constructor
        if (constructorComments.size() > 0) {
            ans.append("    /**");
            for (String elt : constructorComments) {
                ans.append("\n" + elt);
            }
            ans.append("\n     */\n");
        }
        ans.append("    public "+className+"(Client client");
        ans.append(constructorHeader);
        ans.append(") {\n        super(client, new HttpMethod(\""+httpMethod+"\"));\n");
        if (addFormatMsgpack) {
            ans.append("        addQuery(\"format\", \"msgpack\");\n");
        }

        ans.append(constructorBody);
        ans.append("    }\n\n");

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

    // Write the class of a path expression
    // This is the root method for preparing the complete class
    void writeQueryClass(
            StringBuffer generatedPathsEntry,
            JsonNode spec,
            String path,
            String directory,
            String pkg,
            String modelPkg) throws IOException {

        String httpMethod;
        Iterator<String> fields = spec.fieldNames();
        httpMethod = fields.next();
        spec = spec.get(httpMethod);

        String className = spec.get("operationId").asText();
        String methodName = Generator.getCamelCase(className, false);
        className = Generator.getCamelCase(className, true);

        JsonNode paramNode = spec.get("parameters");
        String returnType = "String";
        if (spec.get("responses").get("200").get("$ref") != null) {
            returnType = spec.get("responses").get("200").get("$ref").asText();
            JsonNode returnTypeNode = this.getFromRef(returnType);
            if (returnTypeNode.get("schema").get("$ref") != null) {
                returnType = Generator.getTypeNameFromRef(returnTypeNode.get("schema").get("$ref").asText());
            } else {
                returnType = Generator.getTypeNameFromRef(returnType);
                returnType = Generator.getCamelCase(returnType, true);
            }
        }
        String desc = spec.get("description") != null ? spec.get("description").asText() : "";
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
        addImport(imports, "com.algorand.algosdk.v2.client.common.HttpMethod");
        addImport(imports, "com.algorand.algosdk.v2.client.common.Query");
        addImport(imports, "com.algorand.algosdk.v2.client.common.QueryData");
        addImport(imports, "com.algorand.algosdk.v2.client.common.Response");
        if (needsClassImport(returnType.toLowerCase())) {
            addImport(imports, modelPkg + "." + returnType);
        }

        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append(Generator.formatComment(desc, "", true));
        generatedPathsEntry.append(Generator.formatComment(desc, TAB, true));
        generatedPathsEntry.append("    public " + className + " " + methodName + "(");
        sb.append("public class " + className + " extends Query {\n\n");
        sb.append(
                processQueryParams(
                        generatedPathsEntry,
                        properties,
                        className,
                        path,
                        returnType,
                        httpMethod,
                        imports));
        sb.append("\n}");
        bw.append(getImports(imports));
        bw.append(sb);
        bw.close();
    }

    // Generate all the enum classes in the spec file. 
    public static void generateEnumClasses (JsonNode root, String rootPath, String pkg) throws IOException {

        BufferedWriter bw = getFileWriter("Enums", rootPath);
        bw.append("package " + pkg + ";\n\n");
        bw.append("import com.fasterxml.jackson.annotation.JsonProperty;\n\n");
        bw.append("public class Enums {\n\n");
        JsonNode parameters = root.get("parameters");
        Iterator<Entry<String, JsonNode>> classes = parameters.fields();
        while (classes.hasNext()) {
            Entry<String, JsonNode> cls = classes.next();
            if (cls.getValue().get("enum") != null) {

                // Do not expose format property
                if (cls.getKey().equals("format")) {
                    continue;
                }

                if (cls.getValue().get("description") != null) {
                    String comment = null;
                    comment = cls.getValue().get("description").asText();
                    bw.append(Generator.formatComment(comment, "", true));
                }
                TypeDef enumType = getEnum(cls.getValue(), cls.getKey());
                bw.append(enumType.def);
                bw.append("\n");
            }
        }
        bw.append("}\n");
        bw.close();
    }

    // Generate all the Indexer or algod model classes 
    public void generateAlgodIndexerObjects (JsonNode root, String rootPath, String pkg) throws IOException {
        JsonNode schemas = root.get("components") != null ? 
                root.get("components").get("schemas") : 
                    root.get("definitions");
                Iterator<Entry<String, JsonNode>> classes = schemas.fields();
                while (classes.hasNext()) {
                    Entry<String, JsonNode> cls = classes.next();
                    String desc = null;
                    if (!hasProperties(cls.getValue())) {
                        // If it has no properties, no class is needed for this type.
                        continue;
                    }
                    if (cls.getValue().get("description") != null) {
                        desc = cls.getValue().get("description").asText();
                        desc = formatComment(desc, "", true);
                    }
                    writeClass(cls.getKey(), cls.getValue().get("properties"), desc, rootPath, pkg);
                }
    }

    // Generate all the Indexer or algod return type classes 
    public void generateReturnTypes (JsonNode root, String rootPath, String pkg) throws IOException {
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
                            writeClass(rtype.getKey(), rSchema.get("properties"), null, rootPath, pkg);
                }
    }

    // Generate all the path expression classes
    public void generateQueryMethods(String rootPath, String pkg, String modelPkg, File gpImpDirFile, File gpMethodsDirFile) throws IOException {
        // GeneratedPaths file
        try (   BufferedWriter gpImports = new BufferedWriter(new FileWriter(gpImpDirFile));
                BufferedWriter gpMethods = new BufferedWriter(new FileWriter(gpMethodsDirFile))) {
            StringBuffer gpBody = new StringBuffer();

            JsonNode paths = this.root.get("paths");
            Iterator<Entry<String, JsonNode>> pathIter = paths.fields();
            while (pathIter.hasNext()) {
                Entry<String, JsonNode> path = pathIter.next();
                JsonNode privateTag = path.getValue().get("post") != null ? path.getValue().get("post").get("tags") : null;
                if (privateTag != null && privateTag.elements().next().asText().equals("private")) {
                    continue;
                }
                writeQueryClass(gpBody, path.getValue(), path.getKey(), rootPath, pkg, modelPkg);

                // Fill GeneratedPaths class
                String className = getCamelCase(path.getValue().findPath("operationId").asText(), true);
                gpImports.append("import " + pkg + "." + className + ";\n");
            }
            gpMethods.append(gpBody);
        }
    }

    public Generator (JsonNode root) {
        this.root = root;
    }
}
