package com.algorand.sdkutils.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;

public class TemplateGenerator extends Generator{

	public TemplateGenerator(JsonNode root) {
		super(root);
	}

	public void writeTemplate(String filename) throws IOException {
		File outFile = new File(filename+".csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

		File outFile2 = new File(filename+"Notes.txt");
		BufferedWriter notes = new BufferedWriter(new FileWriter(outFile2));
		
		JsonNode paths = this.root.get("paths");
		Iterator<Entry<String, JsonNode>> pathIter = paths.fields();
		while (pathIter.hasNext()) {
			Entry<String, JsonNode> path = pathIter.next();
			String pathString = path.getKey();
			
			// Get the description
			String desc = null;
			if (path.getValue().get("post") != null) {
				if (path.getValue().get("post").get("description") != null) {
					desc = path.getValue().get("post").get("description").asText();
				}
			} else {
				if (path.getValue().get("get").get("description") != null) {
					desc = path.getValue().get("get").get("description").asText();
				}
			}
			
			if (desc != null) {
				notes.append(Generator.formatComment(desc, "")+"\n");
			}
			notes.append(pathString+"\n");
			
			JsonNode paramNode = path.getValue().findValue("parameters");
			Iterator<Entry<String, JsonNode>> properties = getSortedParameters(paramNode);
			bw.append(pathString + ": ");
			while (properties.hasNext()) {
				Entry<String, JsonNode> parameter = properties.next();
				bw.append(parameter.getKey());
				bw.append("(" + parameter.getValue().get("type").asText() + ")");
				if (isRequired(parameter.getValue())) {
					bw.append("[R]");
				}
				bw.append(", ");
				if (parameter.getValue().get("description") != null) {
					notes.append(Generator.formatComment(parameter.getValue().get("description").asText(), "\t")+"\n");
					notes.append("\t"+parameter.getKey()+"\n");
				}
			}
			bw.append("\n\n\n");
			notes.append("\n\n");
		}
		notes.close();
		bw.close();
	}

}
