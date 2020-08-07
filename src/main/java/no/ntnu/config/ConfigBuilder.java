package no.ntnu.config;

import no.ntnu.config.configBuilder.ConfigFileParam;
import no.ntnu.config.configBuilder.ConfigIntParam;
import no.ntnu.config.configBuilder.ConfigParam;
import no.trygvejw.simpleUi.SimpleUserInput;
import no.trygvejw.simpleUi.simpleTable.Column;
import no.trygvejw.simpleUi.simpleTable.SimpleTable;
import no.trygvejw.util.FileUtils;

import java.io.File;
import java.util.Arrays;


/**
 * A CLI class to allow a user to change the config for his/her project
 * <p>
 * The config wil warn the user if they are doing something incorrectly and suggest they change it. (actually doing it is optional)
 */
public class ConfigBuilder {

    private static final String runTypeName = "Runtype";

    /**
     * This starts the configuration process assuming there are no previous config present i.e. the user is asked to
     * specify run type and subsequently shown the defaults for the selected type.
     *
     * @return The configured Api config object
     */
    public static ApiConfig makeUserBuildConfig() {
        return ConfigBuilder.updateConfig(ConfigBuilder.queryForRunType());
    }


    /**
     * Starts the configuration loop allowing the user to change the config
     * the loop is:
     * Shown config  -> asked if ok | - no -> asked what to change and allowed to change that -> returned to show config
     *                              | - yes -> | - the config is valid -> config is returned.
     *                                         | - the config is invalid -> the user is asked if want to send invalid config | -yes-> config is returned
     *                                         | -no -> returned to show config
     *
     * @param apiConfig the current config
     * @return the configured config
     */
    public static ApiConfig updateConfig(ApiConfig apiConfig) {
        /*
        This is Ig not a very good method:
         - it is to long
         - the whole runTypeName thing is stupid, unreliable and should be changed for a ConfigEnumParam class
         - its super messy, the whole special case stuff should be easier to extend and read
         */


        ApiConfig activeConfig = apiConfig;
        SimpleUserInput userInput = new SimpleUserInput();


        // building the table
        SimpleTable<ConfigParam> configTable = new SimpleTable<>();
        configTable.setShowRowIndex(true);

        //noinspection unchecked
        configTable.setCols(
                new Column<>("config type", ConfigParam::getConfigName),
                new Column<>("Config value", ConfigParam::getStringValue)
        );


        configTable.setTitle("Current config");


        //fill with items
        configTable.setItems(Arrays.asList(activeConfig.getConfigRows()));

        boolean configOK = false;

        while (!configOK) {
            configTable.display();

            configOK = userInput.getYesNoInput("Is current config OK ", true);
            if (!configOK) {
                int configToChange = userInput.getIntInput("row to change: ", 1, configTable.getItems().size() + 1) - 1;

                ConfigParam configParam = configTable.getItems().get(configToChange);
                String changerName = configParam.getConfigName();

                if (changerName.equals(ConfigBuilder.runTypeName)) {
                    boolean changeRT = userInput.getYesNoInput("Changing runtype wil reset all configs to default, you probably do not want to do this. continu anyway? ", false);
                    if (changeRT) {
                        activeConfig = ConfigBuilder.makeUserBuildConfig();
                        configTable.setItems(Arrays.asList(activeConfig.getConfigRows()));

                    }
                } else {

                    if (configParam instanceof ConfigIntParam) {
                        configParam.setFromStringValue(String.valueOf(
                                userInput.getIntInput(String.format("input int value for %s param: ", changerName))));
                    } else if (configParam instanceof ConfigFileParam) {
                        boolean validFileInput = false;
                        while (!validFileInput) {
                            String userFileInput = userInput.getStringInputt("Input file name: ").trim();
                            if (userFileInput.startsWith("'") && userFileInput.endsWith("'")) {
                                // allows for dragging files to the unix terminal
                                userFileInput = userFileInput.substring(1, userFileInput.length() - 1);
                            }
                            File inputFile = new File(userFileInput);

                            // TODO: this probably does not work on windows
                            if (!inputFile.exists()) {
                                validFileInput = userInput.getYesNoInput(String.format("file %s does not exist continue anyway", inputFile.getAbsolutePath()), false);
                            } else if (!FileUtils.isFileChildOfDir(inputFile, FileUtils.executionDir)) {
                                validFileInput = userInput.getYesNoInput(
                                        String.format("file %s is not in current dir %s and wil not be included, continue anyway", inputFile.getAbsolutePath(), FileUtils.executionDir.getAbsolutePath()), false);
                            } else {
                                validFileInput = true;
                                configParam.setFromStringValue(inputFile.getAbsolutePath());
                            }
                        }

                    } else {
                        configParam.setFromStringValue(
                                userInput.getStringInputt(String.format("input value for %s param: ", changerName)));
                    }
                }
                activeConfig.writeConfigToFile();// todo: this is acidental that works try to remove
            } else if (!activeConfig.validateConfig().equals(ConfigError.ok)) {
                ConfigError configError = activeConfig.validateConfig();
                System.out.println("-- Error detected in config --");
                switch (configError) {
                    case mailDomainError:
                        // The white list check is server side. here only the is a mail adress check is done.
                        System.out.println("Invalid mail address provided");
                        break;
                    case noPomError:
                        System.out.println("No pom.xml found in context dir");
                        break;
                    case noValidClasspathSetError:
                        break;
                    case executableDoesNotExistError:
                        System.out.println("No Executable file found in context dir");
                        break;
                }
                configOK = userInput.getYesNoInput("Unless fixed the ticket will most probably not be accepted by the server, try anyway  ", false);
            }
        }

        return activeConfig;
    }

    /**
     * Queries the user what kind of run type they want to use and returns a default config of that type
     *
     * @return a default config of the selected type
     */
    private static ApiConfig queryForRunType() {
        SimpleUserInput userInput = new SimpleUserInput();


        System.out.println("1: JAVA");
        System.out.println("2: PYTHON");

        int newIndex = userInput.getIntInput("select new run type: ", 1, 3);

        ApiConfig newRunType = null;
        switch (newIndex) {
            case 1:
                newRunType = new JavaApiConfig();
                break;
            case 2:
                newRunType = new PythonApiConfig();
                break;
        }


        return newRunType;
    }


}
