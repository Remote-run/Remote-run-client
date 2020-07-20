package no.ntnu.config;

import no.ntnu.config.ApiConfig;
import no.ntnu.config.configBuilder.ConfigFileParam;
import no.ntnu.config.configBuilder.ConfigIntParam;
import no.ntnu.config.configBuilder.ConfigParam;
import no.ntnu.config.configBuilder.ConfigStringParam;
import no.ntnu.enums.RunType;
import no.ntnu.ui.cli.Column;
import no.ntnu.ui.cli.SimpleTable;
import no.ntnu.ui.cli.SimpleUserInput;
import no.ntnu.util.FileUtils;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ConfigBuilder {

    private static final String runTypeName = "Runtype";

    // run if no config present
    public static ApiConfig makeUserBuildConfig(){
        return ConfigBuilder.updateConfig(ConfigBuilder.queryForRuntype());
    }


    /**
     * Allows the user to update values in the user config file
     * @param apiConfig
     * @return
     */
    public static ApiConfig updateConfig(ApiConfig apiConfig){
        ApiConfig activeConfig = apiConfig;
        SimpleUserInput userInput = new SimpleUserInput();


        // bulding the table
        SimpleTable<ConfigParam> configTable = new SimpleTable<>();
        configTable.setShowRowIndex(true);

        configTable.setCols(
                new Column<ConfigParam>("config type", ConfigParam::getConfigName),
                new Column<ConfigParam>("Config value", ConfigParam::getStringValue)
        );


        configTable.setTitle("Current config");


        //fill with items
        configTable.setItems(Arrays.asList(activeConfig.getConfigRows()));

        boolean configOK = false;

        while (!configOK){
            configTable.display();

            configOK = userInput.getYesNoInput("Is current config OK ", true);
            if (!configOK){
                Integer configToChange = userInput.getIntInput("row to change: ", 1, configTable.getItems().size() + 1) - 1;

                ConfigParam configParam = configTable.getItems().get(configToChange);
                String changerName = configParam.getConfigName();

                if (changerName.equals(ConfigBuilder.runTypeName)){
                    boolean changeRT = userInput.getYesNoInput("Changing runtype wil reset all configs to default, you probably do not want to do this. continu anyway? ", false);
                    if (changeRT){
                        activeConfig = ConfigBuilder.makeUserBuildConfig();
                        configTable.setItems(Arrays.asList(activeConfig.getConfigRows()));

                    }
                } else {

                    if (configParam instanceof ConfigIntParam){
                        configParam.setFromStringValue(String.valueOf(
                                userInput.getIntInput(String.format("inputt int value for %s param: ", changerName))));
                    } else if (configParam instanceof ConfigFileParam) {
                        boolean validFileInput = false;
                        while (!validFileInput){
                            String userFileInput = userInput.getStingInputt("Inputt file name: ").trim();
                            if (userFileInput.startsWith("'") && userFileInput.endsWith("'")){
                                // alows for draging files to the unix terminal
                                userFileInput = userFileInput.substring(1, userFileInput.length() - 1);
                            }
                            File inputtFile = new File(userFileInput);

                            // TODO: this probably does not work on windows
                            if (!inputtFile.exists()){
                                validFileInput = userInput.getYesNoInput(String.format("file %s does not exist coninue anyway", inputtFile.getAbsolutePath()), false);
                            } else if (!FileUtils.isFileChildOfDir(inputtFile, FileUtils.executionDir)){
                                validFileInput = userInput.getYesNoInput(
                                        String.format("file %s is not in current dir %s and wil not be included, continue anyway", inputtFile.getAbsolutePath(), FileUtils.executionDir.getAbsolutePath()), false);
                            } else {
                                validFileInput = true;
                                configParam.setFromStringValue(inputtFile.getAbsolutePath().replaceFirst(FileUtils.executionDir.getAbsolutePath(), ""));
                            }
                        }

                    } else {
                        configParam.setFromStringValue(
                                userInput.getStingInputt(String.format("inputt value for %s param: ", changerName)));
                    }
                }
                activeConfig.writeConfig();
            }
        }
        return activeConfig;
    }

    private static ApiConfig queryForRuntype(){
        SimpleUserInput userInput = new SimpleUserInput();


        System.out.println("1: JAVA");
        System.out.println("2: PYTHON");

        int newIndex = userInput.getIntInput("select new run type: ", 1,3);

        ApiConfig newRunType = switch (newIndex){
            case 1 -> new JavaApiConfig();
            case 2 -> new PythonApiConfig();
            default -> null;
        };

        return newRunType;
    }




}
