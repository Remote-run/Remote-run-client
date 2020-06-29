package no.ntnu.config;

import no.ntnu.enums.RunType;
import org.json.JSONObject;

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
        if (this.configFile.exists()){
            try {
                FileReader reader = new FileReader(super.configFile);
                JSONObject loadObject = new JSONObject(reader.read());
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
            FileWriter writer = new FileWriter(super.configFile);
            JSONObject saveObject = super.writeCommonJsonObj();
            writer.write(saveObject.toString());
            writer.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
