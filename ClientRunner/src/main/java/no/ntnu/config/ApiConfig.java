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
import java.util.Arrays;
import java.util.List;
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

    public static File configFile = new File(".config");


    protected ArrayList<File> packingList = new ArrayList<>();

    private enum configParams {
        packingList,
        returnMail,
        runType,
        priority,
    }

    protected String returnMail = "mail";
    protected RunType runType = RunType.UNDEFINED;
    protected int priority = 0;

    public ApiConfig() {}

    public ApiConfig(File configFile) {
        this.configFile = configFile;
    }

    public ArrayList<File> getPackingList() {
        return packingList;
    }

    public void setPackingList(ArrayList<File> packingList) {
        this.packingList = packingList;
        writeConfig();
    }

    public String getReturnMail() {
        return returnMail;
    }


    public void setReturnMail(String returnMail) {
        this.returnMail = returnMail;
        writeConfig();
    }

    public RunType getRunType() {
        return runType;
    }

    public static RunType getRunType(File configFile) throws ParseException, IOException {
        FileReader reader = new FileReader(configFile);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        RunType ret = RunType.valueOf((String) jsonObject.get(configParams.runType.name()));
        reader.close();
        return ret;
    }

    protected void  setRunType(RunType runType){
        this.runType = runType;
        writeConfig();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
        writeConfig();
    }


    protected void readCommonJsonObj(JSONObject jsonObject)  {
        this.packingList.clear();
        ((JSONArray) jsonObject.get(configParams.packingList.name()))
                .stream()
                .forEach((stringPath) -> {
                    File maybeFile = new File((String) stringPath);
                    if (maybeFile.exists())
                        this.packingList.add(maybeFile);
                });

        this.returnMail = (String) jsonObject.get(configParams.returnMail.name());
        this.runType = RunType.valueOf((String) jsonObject.get(configParams.runType.name()));
        this.priority = ((Long) jsonObject.get(configParams.priority.name())).intValue();
    }

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


    protected abstract void readConfig();

    protected abstract void writeConfig();

}
