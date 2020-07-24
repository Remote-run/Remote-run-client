package no.ntnu.config;

import no.ntnu.config.configBuilder.ConfigIntParam;
import no.ntnu.config.configBuilder.ConfigParam;
import no.ntnu.config.configBuilder.ConfigStringParam;
import no.ntnu.enums.RunType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
     * this is purly to avoid having to hunt and peck i decide to change theese later
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
     * @param configFile the file to buld the config from
     */
    public ApiConfig(File configFile) {
        if (configFile.isFile()){
            this.configFile = configFile;
        } else {
            // TODO: this super shold not fail silently and use defalts
            this.configFile = new File(configFile, commonConfigName);
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
     * Read the run type directly from a config file without bulding the object
     * @param configFile the file to read from
     * @return the RunType for the config
     * @throws ParseException error parsing the JSON file
     * @throws IOException
     */
    public static RunType getRunType(File configFile) throws ParseException, IOException {
        FileReader reader = new FileReader(configFile);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        RunType ret = RunType.valueOf((String) jsonObject.get(configParams.runType.name()));
        reader.close();
        return ret;
    }

    /**
     * Read the return mail directly from a config file without bulding the object
     * @param configFile the file to read from
     * @return the return mail for the config
     * @throws ParseException error parsing the JSON file
     * @throws IOException
     */
    public static String getReturnMail(File configFile) throws ParseException, IOException {
        FileReader reader = new FileReader(configFile);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        String ret = (String) jsonObject.get(configParams.returnMail.name());
        reader.close();
        return ret;
    }

    /**
     * Returns a array containg all the config params for this config. This is usually used in a table
     * @return a array containg all the config params for this config.
     */
    public ConfigParam[] getConfigRows(){
        ConfigParam[] rows = new ConfigParam[]{
                new ConfigIntParam("Priority", this::getPriority,this::setPriority),
                new ConfigStringParam("Runtype", () -> this.getRunType().name(), s -> {}),
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
        switch (state){
            case ok:
                return validateCommonConfig();
            default:
                return state;
        }
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
     * Cheks if the current mail is is in the valid domain if one is set, else chek for a @
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
        this.returnMail = (String) jsonObject.get(configParams.returnMail.name());
        this.runType = RunType.valueOf((String) jsonObject.get(configParams.runType.name()));
        this.priority = ((Long) jsonObject.get(configParams.priority.name())).intValue();
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
