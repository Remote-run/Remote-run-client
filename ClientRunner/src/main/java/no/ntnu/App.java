package no.ntnu;

import no.ntnu.config.JavaApiConfig;
import no.ntnu.util.Compression;

import java.io.File;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            System.out.println("Hello World!");
            //ArrayList<ClassPath> ret = new ArrayList(Arrays.asList(Meta.probeForClassPath(new File("/home/trygve/dev/projects/PredatorPreySimulation/src"))));
            //ret.forEach(classPath -> System.out.println(classPath.asDotFormat()));
            File testUncompressedDir = new File("/home/trygve/Development/projects/run-on-server-java-dl4j-example");
            File pom = new File("/home/trygve/Development/projects/run-on-server-java-dl4j-example");
            //JavaApiConfig config = new JavaApiConfig();
            //config.getPackingList().add(testUncompressedDir);
            //System.out.println(config.getPackingList());

            //File testbase = new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/");

            //System.out.println(testbase.getCanonicalPath());
            //File outfile = new File("/home/trygve/tmp/test.gzip");

            //Compression.gZip(testUncompressedDir, outfile );
            //Compression.unzip(outfile, new File("/home/trygve/tmp/out"));

            //Rest.testSendFile(outfile);





        }catch (Exception e){e.printStackTrace();}


    }
}
