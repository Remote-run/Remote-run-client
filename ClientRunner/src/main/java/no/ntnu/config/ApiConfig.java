package no.ntnu.config;

import no.ntnu.enums.RunType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static RunType getRunType(File configFile) throws IOException {
        FileReader reader = new FileReader(configFile);
        JSONObject jsonObject = new JSONObject(reader.read());
        RunType ret = RunType.valueOf((String) jsonObject.get(configParams.runType.name()));
        reader.close();
        return ret;
    }

    private void  setRunType(RunType runType){
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

    private enum configParams {
        packingList,
        returnMail,
        runType,
        priority,
    }


    protected void readCommonJsonObj(JSONObject jsonObject) throws JSONException {
        this.packingList.clear();
        jsonObject.getJSONArray(configParams.packingList.name())
                .iterator()
                .forEachRemaining((stringPath) -> {
                    File maybeFile = new File((String) stringPath);
                    if (maybeFile.exists())
                        this.packingList.add(maybeFile);
                });

        this.returnMail = (String) jsonObject.get(configParams.returnMail.name());
        this.runType = RunType.valueOf((String) jsonObject.get(configParams.runType.name()));
        this.priority = (Integer) jsonObject.get(configParams.priority.name());
    }

    protected JSONObject writeCommonJsonObj() throws IOException {
        JSONObject retObj = new JSONObject();

        JSONArray packingList = new JSONArray(this.packingList
                .stream()
                .filter(file -> file.exists())
                .map(file -> {
                    try{
                        return file.getCanonicalPath();
                    }catch (Exception e){ return null;} // this wil never happen because of the filter above
                }).toArray(String[]::new));
        retObj.put(configParams.packingList.name(), packingList);
        retObj.put(configParams.returnMail.name(), this.returnMail);
        retObj.put(configParams.runType.name(), this.runType.name());
        retObj.put(configParams.priority.name(), this.priority);

        return retObj;
    }

    protected abstract void readConfig();

    protected abstract void writeConfig();

}
