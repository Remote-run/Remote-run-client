package no.ntnu;

import no.ntnu.api.Rest;
import no.ntnu.config.ConfigBuilder;
import no.ntnu.config.JavaApiConfig;
import no.ntnu.config.configBuilder.ConfigParam;
import no.ntnu.config.configBuilder.ConfigStringParam;
import no.ntnu.ui.cli.Column;
import no.ntnu.ui.cli.SimpleTable;
import no.ntnu.util.Compression;
import no.ntnu.util.DebugLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Hello world!
 *
 */
public class clienttestingstuff
{
    public static void main( String[] args )
    {
        try {
            DebugLogger dbg = new DebugLogger(true);
            System.out.println("Hello World!");
            //ArrayList<ClassPath> ret = new ArrayList(Arrays.asList(Meta.probeForClassPath(new File("/home/trygve/dev/projects/PredatorPreySimulation/src"))));
            //ret.forEach(classPath -> System.out.println(classPath.asDotFormat()));
            File testUncompressedDir = new File("/home/trygve/Development/projects/run-on-server-java-dl4j-example");
            File pom = new File("/home/trygve/Development/projects/run-on-server-java-dl4j-example");


            JavaApiConfig config = new JavaApiConfig();
            //config.getPackingList().add(testUncompressedDir);

            ConfigBuilder.makeUserBuildConfig();
            //System.out.println(config.getPackingList());

            /*
            SimpleTable<ConfigStringParam> configTable = new SimpleTable<>();

            configTable.setCols(
                    new Column<>("config type", ConfigStringParam::getConfigName),
                    new Column<>("Config value", ConfigStringParam::getStringValue)
            );

            configTable.addItem(new ConfigStringParam("Priority", () -> String.valueOf(config.getPriority()), s -> config.setPriority(Integer.valueOf(s))));
            configTable.addItem(new ConfigStringParam("return mail", () -> config.getReturnMail(), s -> config.setReturnMail(s)));
            configTable.addItem(new ConfigStringParam("runtype", () -> config.getRunType().name(), s -> {}));

            configTable.display();

             */

            //File testbase = new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/");

            //System.out.println(testbase.getCanonicalPath());
            //File outfile = new File("/home/trygve/tmp/test.gzip");

            //Compression.gZip(testUncompressedDir, outfile );
            //Compression.unzip(outfile, new File("/home/trygve/tmp/out"));

            //System.out.println(ApiConfig.getRunType(ApiConfig.configFile));

            //JavaApiConfig aa = new JavaApiConfig(ApiConfig.configFile);

            //File sendfile = Packager.packageDir(config);
            //Compression.unzip(sendfile, new File("./testout"));
            //Rest.testSendFile(sendfile);






        }catch (Exception e){e.printStackTrace();}


    }
}
