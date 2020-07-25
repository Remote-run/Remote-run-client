package no.ntnu.config;

import no.ntnu.config.configBuilder.ConfigIntParam;
import no.ntnu.config.configBuilder.ConfigParam;
import no.ntnu.config.configBuilder.ConfigStringParam;
import no.ntnu.enums.RunType;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.json.JSONTokener;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Stream;



/**
 * The abstract base class for all the other config files, contains the common configs
 * for the different run types, and getters/setters for these
 *
 */
public abstract class ApiConfig {


    public static final String commonConfigName = ".config";
    public static File configFile = new File(commonConfigName);



    /**
     * the indexes used for saving the config.
     * this is purely to avoid having to hunt and peck i decide to change these later
     */
    private enum configParams {
        returnMail,
        runType,
        priority,
    }

    protected String returnMail = "mail";
    protected RunType runType;
    protected int priority = 0;

    /**
     * Create a api config with all values default;
     */
    public ApiConfig(RunType runType) {
        this.runType = runType;
    }

    /**
     * Creates a api config from a config file
     * @param configFile the file to build the config from
     */
    public ApiConfig(File configFile) {
        if (configFile.isFile()){
            ApiConfig.configFile = configFile;
        } else {
            System.out.println("Error reading config file using defaults");
            ApiConfig.configFile = new File(configFile, commonConfigName);
        }

    }




    /**
     * Returns the return mail for the user.
     * @return The return mail for the user.
     */
    public String getReturnMail() {
        return returnMail;
    }

    /**
     * Sets the return mail.
     * @param returnMail the users return mail.
     */
    public void setReturnMail(String returnMail) {
        this.returnMail = returnMail;
        writeConfig();
    }

    /**
     * Returns the run type.
     * @return The run type.
     */
    public RunType getRunType() {
        return runType;
    }

    /**
     * Returns the run priority
     * @return the run priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * sets the run priority
     * @param priority the run priority
     */
    public void setPriority(int priority){
        this.priority = priority;
        writeConfig();
    }

    /**
     * Read the run type directly from a config file without building the object
     * @param configFile the file to read from
     * @return the RunType for the config
     * @throws IOException If an io error occurs reading the config file
     */
    public static RunType getRunType(File configFile) throws IOException {
        FileReader reader = new FileReader(configFile);
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
        RunType ret = RunType.valueOf(jsonObject.getString(configParams.runType.name()));
        reader.close();
        return ret;
    }

    /**
     * Read the return mail directly from a config file without building the object
     * @param configFile the file to read from
     * @return the return mail for the config
     * @throws IOException If an io error occurs reading the config file
     */
    public static String getReturnMail(File configFile) throws IOException {
        FileReader reader = new FileReader(configFile);
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
        String ret = jsonObject.getString(configParams.returnMail.name());
        reader.close();
        return ret;
    }

    /**
     * Returns a array containing all the config params for this config. This is usually used in a table
     * @return a array containing all the config params for this config.
     */
    public ConfigParam[] getConfigRows(){
        ConfigParam[] rows = new ConfigParam[]{
                new ConfigIntParam("Priority", this::getPriority,this::setPriority),
                new ConfigStringParam("Run type", () -> this.getRunType().name(), s -> {}),
                new ConfigStringParam("Return mail", this::getReturnMail, this::setReturnMail)
        };

        return Stream.of(rows, getRunTypeConfigRows()).flatMap(Stream::of).toArray(ConfigParam[]::new);
    }

    /**
     * Returns the config error state for the current config parameters.
     * if everything is ok the error state ConfigError.ok is returned
     * @return The config error state for the current config parameters.
     */
    public ConfigError validateConfig(){
        ConfigError state = validateRunTypeConfig();
        if (state == ConfigError.ok) {
            return validateCommonConfig();
        }
        return state;
    }

    /**
     * Validates the common config variables, that is mail atm.
     * @return the config error state of the common ApiConfig variables
     */
    private ConfigError validateCommonConfig(){
        ConfigError state;
        if (isMailValid()){
            state = ConfigError.ok;
        } else {
            state = ConfigError.mailDomainError;
        }

        return state;
    }

    /**
     * Cheks if the current mail is is in the valid domain if one is set, else check for a @
     * @return true if the current set mail is valid.
     */
    private boolean isMailValid(){
        String validDomain = System.getenv("");//TODO: FILL DOMAIN FROM CONFIG
        boolean valid = false;

        if (validDomain != null){
            if (this.returnMail.endsWith(validDomain)){
                valid = true;
            }
        } else {
            // no whitelist so check for something@something.something
            if (this.returnMail.contains("@")){
                valid = true;
            }
        }
        return valid;
    }

    /**
     * Validates the config variables for the run type config
     * @return the config error state for the run type config variable.
     */
    protected abstract ConfigError validateRunTypeConfig();

    /**
     * Reads the comm part of the config from the provided json object in to the ApiConfig.
     * @param jsonObject the json object to read from.
     */
    protected void readCommonJsonObj(JSONObject jsonObject)  {
        this.returnMail = jsonObject.getString(configParams.returnMail.name());
        this.runType = RunType.valueOf(jsonObject.getString(configParams.runType.name()));
        this.priority = jsonObject.getInt(configParams.priority.name());
    }

    /**
     * writes the common parts of the config to a json object
     * @return the JSON objet with the config values
     * @throws IOException if any of the files in the packing list does not exist
     */
    protected JSONObject writeCommonJsonObj() throws IOException {
        JSONObject retObj = new JSONObject();

        retObj.put(configParams.returnMail.name(), this.returnMail);
        retObj.put(configParams.runType.name(), this.runType.name());
        retObj.put(configParams.priority.name(), this.priority);

        return retObj;
    }

    /**
     * Read the config from the default config file
     */
    protected abstract void readConfig();

    /**
     * Write the config to the config file
     */
    protected abstract void writeConfig();

    /**
     * Shows the user the current config
     */
    protected abstract ConfigParam[] getRunTypeConfigRows();



}
