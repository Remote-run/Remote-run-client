package no.ntnu.runner;

import no.ntnu.Packager;
import no.ntnu.api.Rest;
import no.ntnu.config.JavaApiConfig;

import java.io.File;
import java.io.ObjectInputFilter;

public class JavaRunner extends Runner{

    public static void main(String[] args) {
        JavaApiConfig config = new JavaApiConfig(JavaApiConfig.configFile);

        if (!isMailValid(config)){
            config.setReturnMail(queryForMail());
        }

        //config.getPackingList().add(JavaApiConfig.configFile.getParentFile());
        try {
            //File zip = Packager.packageDir(config);

            //Rest.testSendFile(zip);
        } catch (Exception e){
            e.printStackTrace();
        }




    }

    private JavaApiConfig getConfig(){
        return null;
    }


    private boolean isMailValid(){
        return false;
    }
}
