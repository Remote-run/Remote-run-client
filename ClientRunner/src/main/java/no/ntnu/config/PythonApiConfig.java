package no.ntnu.config;

import no.ntnu.config.configBuilder.ConfigFileParam;
import no.ntnu.config.configBuilder.ConfigIntParam;
import no.ntnu.config.configBuilder.ConfigParam;
import no.ntnu.config.configBuilder.ConfigStringParam;
import no.ntnu.enums.RunType;
import no.ntnu.util.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PythonApiConfig extends ApiConfig {



    private File fileToExecute = new File("main.py");
    private String executionArgs = "";
    private String image = "nvidia/cuda:10.1-runtime";

    private enum pythonConfigParams{
        fileToExecute,
        executionArgs,
        image,
    }

    public PythonApiConfig() {
        super(RunType.PYTHON);
        readConfig();
    }

    public PythonApiConfig(File configFile) {
        super(configFile);
        readConfig();
    }



    /**
     * Returns the file to execute.
     * @return The file to execute.
     */
    public File getFileToExecute() {
        return fileToExecute;
    }

    /**
     * Sets the file to execute.
     * @param fileToExecute the file to execute.
     */
    public void setFileToExecute(File fileToExecute) {
        this.fileToExecute = fileToExecute;
    }

    /**
     * Returns the execution args.
     * @return The execution args.
     */
    public String getExecutionArgs() {
        return executionArgs;
    }

    /**
     * Sets the execution args.
     * @param executionArgs The execution args.
     */
    public void setExecutionArgs(String executionArgs) {
        this.executionArgs = executionArgs;
    }

    /**
     * Returns the image.
     * @return The image.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image.
     * @param image The image.
     */
    public void setImage(String image) {
        this.image = image;
    }


    /**
     * Reads the config from the save file
     */
    @Override
    protected void readConfig(){
        if (configFile.exists()){
            try {
                JSONParser jsonParser = new JSONParser();
                FileReader reader = new FileReader(configFile);
                JSONObject loadObject = (JSONObject) jsonParser.parse(reader);

                // let the super handle it's arguments
                super.readCommonJsonObj(loadObject);

                this.fileToExecute = new File((String) loadObject.get(pythonConfigParams.fileToExecute.name()));
                this.executionArgs = (String) loadObject.get(pythonConfigParams.executionArgs.name());
                this.image = (String) loadObject.get(pythonConfigParams.image.name());

                reader.close();
            } catch (Exception e){
                System.out.println("ERROR READING CONFIG: " + e.getMessage());
                System.out.println("Using defaults");
                super.runType = RunType.PYTHON;
                writeConfig();
            }
        } else {
            super.runType = RunType.PYTHON;
            writeConfig();
        }


    }

    /**
     * Writes the config to a save file
     */
    @Override
    protected void writeConfig(){
        try {
            FileWriter writer = new FileWriter(configFile);
            JSONObject saveObject = super.writeCommonJsonObj();

            saveObject.put(pythonConfigParams.fileToExecute.name(), this.fileToExecute.getCanonicalPath());
            saveObject.put(pythonConfigParams.executionArgs.name(), this.executionArgs);
            saveObject.put(pythonConfigParams.image.name(), this.image);

            writer.write(saveObject.toString());
            writer.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    protected ConfigParam[] getRunTypeConfigRows() {
        ConfigParam[] rows = new ConfigParam[]{
                new ConfigStringParam("Image", this::getImage,this::setImage),
                new ConfigFileParam("File To execute", this::getFileToExecute, this::setFileToExecute),
                new ConfigStringParam("Execution args", this::getExecutionArgs, this::setExecutionArgs)
        };
        return rows;
    }

    @Override
    protected ConfigError validateRunTypeConfig() {
        if (!this.doesExecutableExist()){
            return ConfigError.executableDoesNotExistError;
        } else {
            return ConfigError.ok;
        }
    }

    private boolean doesExecutableExist(){
        return fileToExecute.exists();
    }


}
