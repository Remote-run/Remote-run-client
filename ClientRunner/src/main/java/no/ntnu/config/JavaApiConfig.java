package no.ntnu.config;

import no.ntnu.config.configBuilder.ConfigParam;
import no.ntnu.enums.RunType;
import no.ntnu.util.FileUtils;
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
        readConfig();
    }

    /**
     * Creates a JavaApiConfig from a config file.
     * if the Config is unable to load from the file the default values are used
     *
     * @param configFile the file to load the config from
     */
    public JavaApiConfig(File configFile) {
        super(configFile);
        readConfig();
    }


    /**
     * Tries to read the config from a file in the current context with the common config name.
     * If no such file exist or there are some errors reading the default config is used
     */
    @Override
    protected void readConfig() {
        if (configFile.exists()) {
            try {
                FileReader reader = new FileReader(configFile);
                JSONObject loadObject = new JSONObject(new JSONTokener(reader));
                super.readCommonJsonObj(loadObject);
                reader.close();
            } catch (Exception e) {
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

    /**
     * Writes the current config to a file with the common config name in the current context
     */
    @Override
    protected void writeConfig() {
        try {
            FileWriter writer = new FileWriter(configFile);
            JSONObject saveObject = super.writeCommonJsonObj();
            saveObject.write(writer);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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
