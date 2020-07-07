package no.ntnu;

import no.ntnu.config.JavaApiConfig;
import no.ntnu.util.DebugLogger;

import java.io.File;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
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

            ArrayList<String> cars = new ArrayList<String>();
            cars.add("Volvo");
            cars.add("BMW");
            cars.add("Ford");
            cars.add("Mazda");

            String aa = "aaaaa";
            int bb = 55;
            dbg.log(testUncompressedDir, pom, aa, bb, cars);


            JavaApiConfig config = new JavaApiConfig();
            config.getPackingList().add(testUncompressedDir);
            //System.out.println(config.getPackingList());

            //File testbase = new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/");

            //System.out.println(testbase.getCanonicalPath());
            //File outfile = new File("/home/trygve/tmp/test.gzip");

            //Compression.gZip(testUncompressedDir, outfile );
            //Compression.unzip(outfile, new File("/home/trygve/tmp/out"));

            //System.out.println(ApiConfig.getRunType(ApiConfig.configFile));

            //JavaApiConfig aa = new JavaApiConfig(ApiConfig.configFile);

            //File sendfile = Packager.packageDir(config);
            //Rest.testSendFile(sendfile);





        }catch (Exception e){e.printStackTrace();}


    }
}
