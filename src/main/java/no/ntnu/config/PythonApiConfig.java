package no.ntnu.config;

import no.ntnu.config.configBuilder.ConfigFileParam;
import no.ntnu.config.configBuilder.ConfigParam;
import no.ntnu.config.configBuilder.ConfigStringParam;
import no.ntnu.enums.RunType;
import no.trygvejw.util.FileUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;


/**
 * The implementation of the Api config for running java code remotely.
 * <p>
 * In addition to the standard ApiConfig the python config includes the exec file, exec args and image to run
 */
public class PythonApiConfig extends ApiConfig {


    private File fileToExecute = new File("main.py");
    private String executionArgs = "";
    private String image = "nvidia/cuda:10.1-runtime";

    /**
     * Creates a python api config with all default values
     */
    public PythonApiConfig() {
        super(RunType.PYTHON);
        super.readConfigFromFile();
    }

    /**
     * Creates a PythonApiConfig from a config file.
     * if the Config is unable to load from the file the default values are used
     *
     * @param configFile the file to load the config from
     */
    public PythonApiConfig(File configFile) {
        super(configFile);
        super.readConfigFromFile();
    }

    /**
     * Returns the file to execute.
     *
     * @return The file to execute.
     */
    public File getFileToExecute() {
        return fileToExecute;
    }

    /**
     * Sets the file to execute.
     *
     * @param fileToExecute the file to execute.
     */
    public void setFileToExecute(File fileToExecute) {
        this.fileToExecute = fileToExecute;
    }

    /**
     * Returns the execution args.
     *
     * @return The execution args.
     */
    public String getExecutionArgs() {
        return executionArgs;
    }

    /**
     * Sets the execution args.
     *
     * @param executionArgs The execution args.
     */
    public void setExecutionArgs(String executionArgs) {
        this.executionArgs = executionArgs;
    }

    /**
     * Returns the image.
     *
     * @return The image.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image.
     *
     * @param image The image.
     */
    public void setImage(String image) {
        this.image = image;
    }


    @Override
    protected void readRunTypeConfig(JSONObject jsonObject) {
        this.fileToExecute = new File(jsonObject.getString(pythonConfigParams.fileToExecute.name()));
        this.executionArgs = jsonObject.getString(pythonConfigParams.executionArgs.name());
        this.image = jsonObject.getString(pythonConfigParams.image.name());

    }


    @Override
    protected void writeRunTypeConfig(JSONObject jsonObject) {
        try {
            jsonObject.put(pythonConfigParams.fileToExecute.name(), this.fileToExecute.getCanonicalPath());
        } catch (Exception e){}

        jsonObject.put(pythonConfigParams.executionArgs.name(), this.executionArgs);
        jsonObject.put(pythonConfigParams.image.name(), this.image);
    }

    /**
     * returns the run type config rows for this config type.
     * These rows can be put in a Simple table to display
     *
     * @return an array containing the rows
     */
    @Override
    protected ConfigParam[] getRunTypeConfigRows() {
        return new ConfigParam[]{
                new ConfigStringParam("Image", this::getImage, this::setImage),
                new ConfigFileParam("File To execute", this::getFileToExecute, this::setFileToExecute),
                new ConfigStringParam("Execution args", this::getExecutionArgs, this::setExecutionArgs)
        };

    }

    /**
     * Validates the if the config is in a valid run state. Currently the validation checks:
     * - does the executable file exist
     *
     * @return the config error code of the validation, if nothing is wrong the error code ok is returned
     */
    @Override
    protected ConfigError validateRunTypeConfig() {
        if (!this.doesExecutableExist()) {
            return ConfigError.executableDoesNotExistError;
        } else {
            return ConfigError.ok;
        }
    }

    /**
     * Returns true if the executable file config param is a file that exists
     *
     * @return true if the executable file config param is a file that exists
     */
    private boolean doesExecutableExist() {
        return new File(FileUtils.executionDir, fileToExecute.getPath()).exists();
    }

    private enum pythonConfigParams {
        fileToExecute,
        executionArgs,
        image,
    }


}
