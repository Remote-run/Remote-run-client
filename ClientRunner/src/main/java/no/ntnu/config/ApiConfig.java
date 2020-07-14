package no.ntnu.config;

import no.ntnu.enums.RunType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/*
brainstorming hva trenger jeg

- = server
+ = client

## alle
- return mail
- Språk/runtype
- priority
+ hva som skal pakkes med

## java


## python
- path til executable
- requierments.txt


## andre språk
-ta seiner


 */

/**
 * The abstract base class for all the other config files, contains the common configs
 * for the different run types, and getters/setters for these
 *
 */
public abstract class ApiConfig {

    public static final String commonConfigName = ".config";
    public static File configFile = new File(commonConfigName);

    // possibly remove this an just take the entire parent dir
    protected ArrayList<File> packingList = new ArrayList<>();

    /**
     * the indexes used for saving the config.
     * this is purly to avoid having to hunt and peck i decide to change theese later
     */
    private enum configParams {
        packingList,
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
     * Returns the list of the files and dirs to include in package sent to the server.
     * @return The list of the files and dirs to include in package sent to the server.
     */
    public ArrayList<File> getPackingList() {
        return packingList;
    }

    /**
     * Sets the list of the files and dirs to include in package sent to the server.
     * @param packingList The list of the files and dirs to include in package sent to the server.
     */
    public void setPackingList(ArrayList<File> packingList) {
        this.packingList = packingList;
        writeConfig();
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
     * Reads the comm part of the config from the provided json object in to the ApiConfig.
     * @param jsonObject the json object to read from.
     */
    protected void readCommonJsonObj(JSONObject jsonObject)  {
        this.packingList.clear();
        ((JSONArray) jsonObject.get(configParams.packingList.name()))
                .forEach(stringPath -> {
                    File maybeFile = new File((String) stringPath);
                    if (maybeFile.exists())
                        this.packingList.add(maybeFile);
                });

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

        JSONArray packingList = new JSONArray();
        packingList.addAll(this.packingList
                .stream()
                .filter(file -> file.exists())
                .map(file -> {
                    try{
                        return file.getCanonicalPath();
                    }catch (Exception e){ return null;} // this wil never happen because of the filter above
                }).collect(Collectors.toCollection(ArrayList::new)));


        retObj.put(configParams.packingList.name(), packingList);
        retObj.put(configParams.returnMail.name(), this.returnMail);
        retObj.put(configParams.runType.name(), this.runType.name());
        retObj.put(configParams.priority.name(), this.priority);

        return retObj;
    }

    protected String getDisplaySting(){
        String displayStr =
                String.format(" Run type:    \t  %s", this.getRunType().name()) +
                String.format(" Return Mail: \t  %s", this.getReturnMail()) +
                String.format(" Priority:    \t  %o", this.getPriority());
        return displayStr;
    }


    /**
     * Read the config from the default config file
     */
    protected abstract void readConfig();

    /**
     * Write the config to the config file
     */
    protected abstract void writeConfig();

}
