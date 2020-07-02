package no.ntnu.config;

import no.ntnu.enums.RunType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PythonApiConfig extends ApiConfig {



    private String runString = "no runstring set";

    private enum pythonConfigParams{
        runString
    }

    public PythonApiConfig() {
        super();
        buildConfig();
        this.setRunType(RunType.PYTHON);
    }

    public PythonApiConfig(File configFile) {
        super(configFile);
        buildConfig();
    }

    public String getRunString() {
        return runString;
    }

    public void setRunString(String runString) {
        this.runString = runString;
        writeConfig();
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
                this.runString = (String) loadObject.get(pythonConfigParams.runString.name());
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
            saveObject.put(pythonConfigParams.runString.name(), this.runString);
            writer.write(saveObject.toString());
            writer.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
