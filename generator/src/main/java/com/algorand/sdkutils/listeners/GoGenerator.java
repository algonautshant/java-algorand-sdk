package com.algorand.sdkutils.listeners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import com.algorand.sdkutils.listeners.Publisher.Events;
import com.algorand.sdkutils.utils.StructDef;
import com.algorand.sdkutils.utils.Tools;
import com.algorand.sdkutils.utils.TypeDef;

public class GoGenerator extends Subscriber {
    
    enum Annotation {
        JSON,
        CODEC,
        URL
    }
    
    private static final String TAB = "\t";

    private String folder;
    private BufferedWriter modelWriter;

    // Model file
    private boolean pendingOpenStruct;
    
    // Query files
    private BufferedWriter queryWriter;    
    private TreeMap<String, String> pathParameters;
    private StringBuffer queryFunctions;
    private String currentQueryName;
    private Map<String, Set<String>> imports;

    

    public GoGenerator(String folder, Publisher publisher) throws IOException {
        publisher.subscribe(Events.NEW_MODEL, this);
        publisher.subscribe(Events.NEW_MODEL_PROPERTY, this);
        publisher.subscribe(Events.NEW_QUERY, this);
        publisher.subscribe(Events.QUERY_PARAMETER, this);
        publisher.subscribe(Events.PATH_PARAMETER, this);
        publisher.subscribe(Events.BODY_CONTENT, this);
        publisher.subscribe(Events.END_QUERY, this);

        this.folder = folder;        

        modelWriter = null;
        
        pendingOpenStruct = false;
    }

    public void terminate() {
        if (pendingOpenStruct) {
            append(modelWriter, "}\n");
        }
        closeFile(modelWriter);
        modelWriter = null;
    }

    @Override
    public void onEvent(Events event) {
        switch(event) {
        case END_QUERY:
            endQuery();
            break;
        default:
            throw new RuntimeException("Unemplemented event! " + event);
        }
    }

    @Override
    public void onEvent(Events event, String note) {
        switch(event) {
        case NEW_QUERY:
            newQuery(note);
            break;
        default:
            throw new RuntimeException("Unemplemented event for note! " + event);
        }
    }

    @Override
    public void onEvent(Events event, TypeDef type) {
        switch(event) {
        case NEW_MODEL_PROPERTY:
            newProperty(type, Annotation.JSON);
            break;
        case QUERY_PARAMETER:
            addQueryParameter(type);
            break;
        case PATH_PARAMETER:
            addPathParameter(type);
            break;
        case BODY_CONTENT:
        default:
            throw new RuntimeException("Unemplemented event for TypeDef! " + event);
        }
    }

    @Override
    public void onEvent(Events event, StructDef sDef) {
        switch(event) {
        case NEW_MODEL:
            newModel(sDef);
            break;
        default:
            throw new RuntimeException("Unemplemented event for StructDef! " + event);
        }

    }

    private void newProperty(TypeDef type, Annotation annType) {
        append(modelWriter, Tools.formatComment(type.doc, TAB, true));
        append(modelWriter, TAB + Tools.getCamelCase(type.propertyName, true) + " ");
        append(modelWriter, goType(type.rawTypeName, type.javaTypeName.equals("array")) + " ");
        append(modelWriter, goAnnotation(type.propertyName, annType, type.required));
        append(modelWriter, "\n");
    }

    private void newModel(StructDef sDef) {
        if (pendingOpenStruct) {
            append(modelWriter, "}\n\n");
        }
        if (modelWriter == null) {
            modelWriter = newFile("models");   
        }
        if (sDef.doc != null) {
            append(modelWriter, Tools.formatComment(sDef.doc, "", true));
        }
        append(modelWriter, "type " + sDef.name + " struct {\n");
        pendingOpenStruct = true;
    }

    private void newQuery(String name) {
        pathParameters = new TreeMap<String, String>();
        queryFunctions = new StringBuffer();
        imports = new TreeMap<String, Set<String>>();
        
        currentQueryName = Tools.getCamelCase(name, true);
        if (queryWriter != null) {
            throw new RuntimeException("Query writer should be closed!");
        }
        queryWriter = newFile(currentQueryName);
        append(queryWriter, 
                "package indexer\n\n" +
                "import (\n" +
                TAB + "\"context\"\n" +
                TAB + "\"github.com/algorand/go-algorand-sdk/client/v2/common\"\n" +
                TAB + "\"github.com/algorand/go-algorand-sdk/client/v2/common/models\"\n" +
                ")\n\n");

        append(queryWriter, "type " + currentQueryName + " struct {\n");

        pathParameters = new TreeMap<String, String>();
        pathParameters.put("c", "*Client");
        pathParameters.put("p", "models." + currentQueryName + "Params");
        
        // Also need to create the stcut for the parameters
        newModel(new StructDef(currentQueryName + "Params", ""));
    }
    
    private void addPathParameter(TypeDef type) {
        pathParameters.put(
                Tools.getCamelCase(type.propertyName, false),
                goType(type.rawTypeName, type.javaTypeName.equals("array")));
                
    }
    
    private void addQueryParameter(TypeDef type) {
        
        // Also need to add this to the path struct (model)
       newProperty(type, Annotation.URL);
       
       String funcName = Tools.getCamelCase(type.propertyName, true);
       String paramName = Tools.getCamelCase(type.propertyName, false);
       String desc = Tools.formatComment(type.doc, "", true);
       TypeConverter typeConv = goType(type.rawTypeName, type.javaTypeName.equals("array"), 
               true, paramName);

        
       append(queryFunctions, desc);
       append(queryFunctions, 
               "func (s *" + currentQueryName + ") " + 
                       funcName + "(" + paramName + " " + typeConv.type + ") " + 
                       "*" + currentQueryName + " {\n");
       append(queryFunctions, TAB + "s.p." + funcName + " = " + typeConv.converter + "\n");
       append(queryFunctions, TAB + "return s\n}\n\n");
    }
        
    private void endQuery() {
        Iterator<Entry<String, String>> pps = pathParameters.entrySet().iterator();
        while(pps.hasNext()) {
            Entry<String, String> pp = pps.next();
            append(queryWriter, TAB + pp.getKey() + " " + pp.getValue() + "\n");
        }
        append(queryWriter, "}\n\n");
            
        append(queryWriter, queryFunctions.toString());
        closeFile(queryWriter);
        queryWriter = null;
    }

    private BufferedWriter newFile(String filename) {
        String pathName = folder + 
                File.separatorChar +
                filename +
                ".go";
        try {
            return new BufferedWriter(new FileWriter(pathName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void append(Writer bw, String text) {
        try {
            bw.append(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void append(StringBuffer sb, String text) {
        sb.append(text);
    }

    private void closeFile(BufferedWriter bw) {
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String goType(String type, boolean array) {
        return goType(type, array, false, "").type;
    }
    
    class TypeConverter {
        public String type;
        public String converter;
        
        public TypeConverter(String type, String converter) {
            this.type = type;
            this.converter = converter;
        }
    }
    
    TypeConverter goType(String type, boolean array, boolean asType, String paramName) {

        String goType = "";
        String converter = paramName;
        
        switch (type) {
        case "boolean":
            goType = "bool";
            break;
        case "integer":
            goType = "uint64";
            break;
        case "string":
            goType = type;
            break;        
        case "Account":
            goType = "string";
            break;
        case "address":
            goType = asType ? "type.Address" : "string";
            if (asType) {
                addImport("C", "github.com/algorand/go-algorand-sdk/types");
            }
            break;
            
        case "time":
            goType = asType ? "time.Time" : "string";
            if (asType) {
                addImport("A", "time");
            }
            break;
            
        case "byteArray":
            goType = asType ? "[]byte" : "string";
            if (asType) {
                addImport("A", "encoding/base64");
                converter = "base64.StdEncoding.EncodeToString(" + paramName +")";
            }
            break;
            
            
        case "Asset":
        case "AssetHolding":
        case "AssetParams":
        case "AccountParticipation":
        case "Application":
        case "ApplicationLocalState":
        case "ApplicationStateSchema":
        case "ApplicationParams":
        case "BlockRewards":
        case "BlockUpgradeState":
        case "BlockUpgradeVote":
        case "MiniAssetHolding":
        case "TealValue":
        case "TealKeyValue":
        case "Transaction":
        case "TransactionAssetConfig":
        case "TransactionAssetFreeze":
        case "TransactionAssetTransfer":
        case "TransactionKeyreg":
        case "TransactionPayment":
        case "TransactionSignature":
        case "TransactionSignatureLogicsig":
        case "TransactionSignatureMultisig":
        case "TransactionSignatureMultisigSubsignature":
            goType = type;
            break;
        default:
            goType = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx";            
        }
        if (array) {
            return new TypeConverter("[]" + goType, converter);
        }
        return new TypeConverter(goType, converter);
    }

    private String goAnnotation(String propertyName, Annotation annotation, boolean required) {
        String annType = "";
        switch (annotation) {
        case JSON:
            annType = "json";
            break;
        case URL:
            annType = "url";
            break;
        default:
                
        }
        
        String val =  "`"+annType+":\"" + propertyName;
        if (!required) {
            val = val + ",omitempty";
        }
        val = val + "\"`\n";
        return val;
    }

    // Imports are collected and organized before printed as import statements.
    // addImports adds a needed import class. 
    void addImport(String category, String imp) {
        if (imports.get(category) == null) {
            imports.put(category, new TreeSet<String>());
        }
        imports.get(category).add(imp);
    }

    // getImports organizes the imports and returns the block of import statements
    // The statements are unique, and organized. 
    String getImports() {
        StringBuffer sb = new StringBuffer();

        Set<String> catA = imports.get("A");
        if (catA != null) {
            for (String imp : catA) {
                sb.append("import " + imp + "\n");
            }
            sb.append("\n");
        }

        Set<String> catB = imports.get("B");
        if (catB != null) {
            for (String imp : catB) {
                sb.append("import " + imp + "\n");
            }
            sb.append("\n");
        }
        
        Set<String> catC = imports.get("C");
        if (catC != null) {
            for (String imp : catC) {
                sb.append("import " + imp + "\n");
            }
        }
        sb.append("\n");
        return sb.toString();
    }


    
    
    
    
}
