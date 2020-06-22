package org.example;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import netscape.javascript.JSObject;
import org.json.JSONObject;

public class APIConfig {
    private String returnMail;
    private String runPos;
    private String runtype;

    private final String defaultReturnMail = "<mail_addr>";
    private final String defaultRunPos = "./target/*-bin.jar";  // usually shadow jars
    private final String defaultRuntype = "JAVA";


    private File configFile;
    private final String configFP = "./api-config.json";

    private HashMap<String, String> configValues;

    public HashMap<String, String> getConfigValues() {
        return configValues;
    }

    public APIConfig() throws IOException {
        this.configValues = new HashMap<>();

        this.configFile = new File(this.configFP);
        if (this.configFile.createNewFile()){
            // if no config make the file and write defaults
            this.configValues.put("retMail", this.defaultReturnMail);
            this.configValues.put("runPos", this.defaultRunPos);
            this.configValues.put("runType", this.defaultRuntype);

            JSONObject js = new JSONObject(this.configFile);
            FileWriter writer = new FileWriter(this.configFile);
            writer.write(js.toString());
            writer.close();
        } else {
            FileReader reader = new FileReader(this.configFile);
            JSONObject obj = new JSONObject(reader.read());

            this.configValues.put("retMail", obj.get("retMail").toString());
            this.configValues.put("runPos", obj.get("runPos").toString());
            this.configValues.put("runType", obj.get("runType").toString());
        }

    }


}
