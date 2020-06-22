package org.example;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.example.enums.ConfigEnum;
import org.example.enums.RunTypes;
import org.json.JSONObject;

public class APIConfig {

    private String returnMail;
    private String runPos;
    private RunTypes runtype;



    private String srcRoot;

    private final String defaultReturnMail = "<mail_addr>";
    private final String defaultRunPos = "./target/*-bin.jar";  // usually shadow jars
    private final RunTypes defaultRuntype = RunTypes.JAVA;
    private final String defultSrcRoot = "./src/main/java";


    private File configFile;
    public final String configFP = new File("./.apiconfig").getAbsolutePath();

    public String getReturnMail() {
        return returnMail;
    }

    public String getRunPos() {
        return runPos;
    }

    public RunTypes getRuntype() {
        return runtype;
    }

    public String getSrcRoot() {
        return srcRoot;
    }




    public APIConfig() throws IOException {

        this.configFile = new File(this.configFP);
        if (this.configFile.createNewFile()){
            // if no config make the file and write defaults
            this.returnMail = this.defaultReturnMail;
            this.runPos     = this.defaultRunPos;
            this.runtype    = this.defaultRuntype;
            this.srcRoot    = this.defultSrcRoot;

            this.saveConfig();

        } else {
            FileReader reader = new FileReader(this.configFile);
            JSONObject obj = new JSONObject(reader.read());

            this.returnMail = obj.get(ConfigEnum.ReturnMail.name()).toString();
            this.runPos     = obj.get(ConfigEnum.RunPos.name()).toString();
            this.runtype    = RunTypes.valueOf(obj.get(ConfigEnum.RunType.name()).toString());
            this.srcRoot    = obj.get(ConfigEnum.SrcRoot.name()).toString();
        }

    }

    private void saveConfig() throws IOException{
        JSONObject js = new JSONObject();

        js.put(ConfigEnum.ReturnMail.name(), this.returnMail);
        js.put(ConfigEnum.RunPos.name(), this.runPos);
        js.put(ConfigEnum.RunType.name(), this.runtype);
        js.put(ConfigEnum.SrcRoot.name(), this.srcRoot);


        FileWriter writer = new FileWriter(this.configFile);
        writer.write(js.toString());
        writer.close();

    }


}
