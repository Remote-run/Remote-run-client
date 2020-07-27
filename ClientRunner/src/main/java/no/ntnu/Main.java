package no.ntnu;

import no.ntnu.api.Rest;
import no.ntnu.config.ApiConfig;
import no.ntnu.config.ConfigBuilder;
import no.ntnu.config.JavaApiConfig;
import no.ntnu.config.PythonApiConfig;
import no.ntnu.util.Compression;

import java.io.File;
import java.nio.file.Paths;

public class Main {
    /*
    TODO:
        - url må bli en param
        - Skriv en readme som inkludere info om hvordan og legg til nye kjøremåter



     */

    public static void main(String[] args) {
        File configFile = new File(ApiConfig.commonConfigName);
        if (configFile.exists()){


            try{

                ApiConfig config = null;
                switch (ApiConfig.getRunType(configFile)){
                    case JAVA:
                        config = new JavaApiConfig(configFile);
                        break;
                    case PYTHON:
                        config = new PythonApiConfig(configFile);
                        break;
                };
                ConfigBuilder.updateConfig(config);
            } catch (Exception e ){
                e.printStackTrace();
                ConfigBuilder.makeUserBuildConfig();
            }
        }else {
            ConfigBuilder.makeUserBuildConfig();
        }

        File parentDir = Paths.get(".").toFile().getAbsoluteFile().getParentFile();

        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File gzipFileName = new File(tmpDir + "/remote_run.gzip");


        try{
            Compression.gZip(parentDir, gzipFileName);
            Rest.postFile(gzipFileName, "http://localhost:8080/com.example.RemoteRunApiServlet/");
        } catch (Exception e){
            e.printStackTrace();
        }



    }
}
