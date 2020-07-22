package no.ntnu.config;

import no.ntnu.config.configBuilder.ConfigParam;
import no.ntnu.enums.RunType;
import no.ntnu.util.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class JavaApiConfig extends ApiConfig {

    public JavaApiConfig() {
        super(RunType.JAVA);
        buildConfig();
    }

    public JavaApiConfig(File configFile) {
        super(configFile);
        buildConfig();
    }



    private void buildConfig(){
        this.readConfig();
    }

    @Override
    protected void readConfig(){
        if (configFile.exists()){
            try {
                JSONParser jsonParser = new JSONParser();
                FileReader reader = new FileReader(configFile);
                JSONObject loadObject = (JSONObject) jsonParser.parse(reader);
                super.readCommonJsonObj(loadObject);
                reader.close();
            } catch (Exception e){
                System.out.println("ERROR READING CONFIG: " + e.getMessage());
                System.out.println("Using defaults");
                super.runType = RunType.JAVA;
                writeConfig();
            }
        } else {
            super.runType = RunType.JAVA;
            writeConfig();
        }


    }

    @Override
    protected void writeConfig(){
        try {
            FileWriter writer = new FileWriter(configFile);
            JSONObject saveObject = super.writeCommonJsonObj();
            writer.write(saveObject.toString());
            writer.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    protected ConfigParam[] getRunTypeConfigRows() {
        return new ConfigParam[0];
    }

    @Override
    protected ConfigError validateRunTypeConfig() {
        if (!doesPomExist()){
            return ConfigError.noPomError;
        } else if (!isClasspathValid()){
            return ConfigError.noValidClasspathSetError;
        } else {
            return ConfigError.ok;
        }
    }

    private boolean doesPomExist(){
        File mabyePomFile = new File(FileUtils.executionDir, "pom.xml");

        return mabyePomFile.exists();
    }

    // todo: implement, if time.
    private boolean isClasspathValid(){
        return true;
    }
}
