package no.ntnu.config;

import no.ntnu.config.configBuilder.ConfigParam;
import no.ntnu.enums.RunType;
import no.trygvejw.util.FileUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The implementation of the Api config for running java code remotely
 */
public class JavaApiConfig extends ApiConfig {

    /**
     * Creates a JavaApiConfig with all default values
     */
    public JavaApiConfig() {
        super(RunType.JAVA);
        super.readConfigFromFile();
    }

    /**
     * Creates a JavaApiConfig from a config file.
     * if the Config is unable to load from the file the default values are used
     *
     * @param configFile the file to load the config from
     */
    public JavaApiConfig(File configFile) {
        super(configFile);
        super.readConfigFromFile();
    }


    @Override
    protected void readRunTypeConfig(JSONObject jsonObject) { }


    @Override
    protected void writeRunTypeConfig(JSONObject jsonObject) {}

    /**
     * returns the run type config rows for this config type.
     * The java config has no configs atm so an empty array is returned
     *
     * @return an empty array
     */
    @Override
    protected ConfigParam[] getRunTypeConfigRows() {
        return new ConfigParam[0];
    }

    /**
     * Validates the if the config is in a valid run state. Currently the validation checks:
     * - If there are a file named pom.xml in the current context
     *
     * @return the config error code of the validation, if nothing is wrong the error code ok is returned
     */
    @Override
    protected ConfigError validateRunTypeConfig() {
        if (!doesPomExist()) {
            return ConfigError.noPomError;
        } else if (!isClasspathValid()) {
            return ConfigError.noValidClasspathSetError;
        } else {
            return ConfigError.ok;
        }
    }

    /**
     * Checks if a file named pom.xml exists in the current context
     *
     * @return true if it does false if not
     */
    private boolean doesPomExist() {
        File mabyePomFile = new File(FileUtils.executionDir, "pom.xml");

        return mabyePomFile.exists();
    }

    // todo: implement, if time.
    private boolean isClasspathValid() {
        return true;
    }
}
