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

    public static void main(String[] args) {
        File configFile = new File(ApiConfig.commonConfigName);
        if (configFile.exists()){


            try{

                ApiConfig config = switch (ApiConfig.getRunType(configFile)){
                    case JAVA -> new JavaApiConfig(configFile);
                    case PYTHON -> new PythonApiConfig(configFile);
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
            Rest.testSendFile(gzipFileName, "http://localhost:8080/com.example.RemoteRunApiServlet/");
        } catch (Exception e){
            e.printStackTrace();
        }



    }
}
