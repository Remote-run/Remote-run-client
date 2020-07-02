package no.ntnu.config;

import no.ntnu.enums.RunType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class JavaApiConfig extends ApiConfig {

    public JavaApiConfig() {
        super();
        buildConfig();
        this.setRunType(RunType.JAVA);
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
                writeConfig();
            }
        } else {
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
}
