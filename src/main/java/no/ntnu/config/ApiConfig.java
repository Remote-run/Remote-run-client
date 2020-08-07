package no.ntnu.config;

import no.ntnu.config.configBuilder.ConfigIntParam;
import no.ntnu.config.configBuilder.ConfigParam;
import no.ntnu.config.configBuilder.ConfigStringParam;
import no.ntnu.enums.RunType;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.stream.Stream;


/**
 * The abstract base class for all the other config files, contains the common configs
 * for the different run types, and getters/setters for these
 * <p>
 * Also provides methods for getting the config parameters as columns to put in a SimpleTable
 */
public abstract class ApiConfig {

    /**
     * The expected name for the config file
     */
    public static final String commonConfigName = ".config";
    public static File configFile = new File(commonConfigName);
    protected String returnMail = "mail";
    protected RunType runType;
    protected int priority = 0;

    // these tow are only set when generating a new config
    private String resourceKey = "DEFAULT";
    private String url = "_";

    /**
     * Create a api config with all values default;
     */
    public ApiConfig(RunType runType) {
        this.runType = runType;
    }

    /**
     * Creates a api config from a config file
     *
     * @param configFile the file to build the config from
     */
    public ApiConfig(File configFile) {
        if (configFile.isFile()) {
            ApiConfig.configFile = configFile;
        } else {
            System.out.println("Error reading config file using defaults");
            ApiConfig.configFile = new File(configFile, commonConfigName);
        }

    }

    /**
     * Returns the configs resource key
     * @return the configs resource key
     */
    public String getResourceKey() {
        return resourceKey;
    }

    /**
     * Read the resource key directly from a config file without building the object
     *
     * @param configFile the file to read from
     * @return the resource key for the config
     * @throws IOException If an io error occurs reading the config file
     */
    public static String getResourceKey(File configFile) throws IOException {
        FileReader reader = new FileReader(configFile);
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
        String ret = jsonObject.getString(configParams.resourceKey.name());
        reader.close();
        return ret;
    }

    /**
     * Returns the api url
     * @return the api url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the api url
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
        writeConfigToFile();
    }

    /**
     * Sets the configs resource key
     * @param resourceKey the new resource key to set
     */
    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
        writeConfigToFile();
    }

    /**
     * Read the run type directly from a config file without building the object
     *
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
     *
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
     * Returns the return mail for the user.
     *
     * @return The return mail for the user.
     */
    public String getReturnMail() {
        return returnMail;
    }

    /**
     * Sets the return mail.
     *
     * @param returnMail the users return mail.
     */
    public void setReturnMail(String returnMail) {
        this.returnMail = returnMail;
        writeConfigToFile();
    }

    /**
     * Returns the run type.
     *
     * @return The run type.
     */
    public RunType getRunType() {
        return runType;
    }

    /**
     * Returns the run priority
     *
     * @return the run priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * sets the run priority
     *
     * @param priority the run priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
        writeConfigToFile();
    }

    /**
     * Returns a array containing all the config params for this config. This is usually used in a table
     *
     * @return a array containing all the config params for this config.
     */
    public ConfigParam[] getConfigRows() {
        ConfigParam[] rows = new ConfigParam[]{
                new ConfigIntParam("Priority", this::getPriority, this::setPriority),
                new ConfigStringParam("Resource key", this::getResourceKey, this::setResourceKey),
                new ConfigStringParam("Server url", this::getUrl, this::setUrl),
                new ConfigStringParam("Run type", () -> this.getRunType().name(), s -> {
                }),
                new ConfigStringParam("Return mail", this::getReturnMail, this::setReturnMail)


        };

        return Stream.of(rows, getRunTypeConfigRows()).flatMap(Stream::of).toArray(ConfigParam[]::new);
    }

    /**
     * Returns the config error state for the current config parameters.
     * if everything is ok the error state ConfigError.ok is returned
     *
     * @return The config error state for the current config parameters.
     */
    public ConfigError validateConfig() {
        ConfigError state = validateRunTypeConfig();
        if (state == ConfigError.ok) {
            return validateCommonConfig();
        }
        return state;
    }

    /**
     * Validates the common config variables, that is mail atm.
     *
     * @return the config error state of the common ApiConfig variables
     */
    private ConfigError validateCommonConfig() {
        ConfigError state;
        if (isMailValid()) {
            state = ConfigError.ok;
        } else {
            state = ConfigError.mailDomainError;
        }

        return state;
    }

    /**
     * Cheks if the current mail is is in the valid domain if one is set, else check for a @
     *
     * @return true if the current set mail is valid.
     */
    private boolean isMailValid() {
        String validDomain = System.getenv("");//TODO: FILL DOMAIN FROM CONFIG
        boolean valid = false;

        if (validDomain != null) {
            if (this.returnMail.endsWith(validDomain)) {
                valid = true;
            }
        } else {
            // no whitelist so check for something@something.something
            if (this.returnMail.contains("@")) {
                valid = true;
            }
        }
        return valid;
    }

    /**
     * Validates the config variables for the run type config
     *
     * @return the config error state for the run type config variable.
     */
    protected abstract ConfigError validateRunTypeConfig();


    protected void readConfigFromFile() {
        if (configFile.exists()) {
            try {
                FileReader reader = new FileReader(configFile);
                JSONObject loadObject = new JSONObject(new JSONTokener(reader));

                this.returnMail = loadObject.getString(configParams.returnMail.name());
                this.runType = RunType.valueOf(loadObject.getString(configParams.runType.name()));
                this.priority = loadObject.getInt(configParams.priority.name());
                this.resourceKey = loadObject.getString(configParams.resourceKey.name());
                this.url = loadObject.getString(configParams.url.name());

                this.readRunTypeConfig(loadObject);
                reader.close();
            } catch (Exception e) {
                System.out.println("ERROR READING CONFIG: " + e.getMessage());
                System.out.println("Using defaults");
                writeConfigToFile();
            }
        } else {
            writeConfigToFile();
        }

    }


    protected void writeConfigToFile() {
        try {
            FileWriter writer = new FileWriter(configFile);

            JSONObject saveObject = new JSONObject();

            saveObject.put(configParams.returnMail.name(), this.returnMail);
            saveObject.put(configParams.runType.name(), this.runType.name());
            saveObject.put(configParams.priority.name(), this.priority);
            saveObject.put(configParams.resourceKey.name(), this.resourceKey);
            saveObject.put(configParams.url.name(), this.url);

            this.writeRunTypeConfig(saveObject);

            saveObject.write(writer);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Reads the run type config params from the provided jsonObject
     *
     * @param jsonObject the object to read the params from
     */
    protected abstract void readRunTypeConfig(JSONObject jsonObject);


    /**
     * Writes the run type config params to the provided jsonObject
     *
     * @param jsonObject the object to write the params to
     */
    protected abstract void writeRunTypeConfig(JSONObject jsonObject);

    /**
     * Shows the user the current config
     */
    protected abstract ConfigParam[] getRunTypeConfigRows();

    /**
     * the indexes used for saving the config.
     * this is purely to avoid having to hunt and peck i decide to change these later
     */
    private enum configParams {
        returnMail,
        runType,
        priority,
        resourceKey,
        url,
    }


}
