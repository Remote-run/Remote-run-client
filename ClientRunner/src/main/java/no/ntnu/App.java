package no.ntnu;

import no.ntnu.Api.Rest;
import no.ntnu.util.Compression;

import java.io.File;
import java.io.FileInputStream;

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

            File testbase = new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/");

            System.out.println(testbase.getCanonicalPath());
            File outfile = new File("/tmp/test.gzip");
            Compression.gZip(testUncompressedDir, outfile );
            //Compression.unzip(new File(testbase.getCanonicalPath() + File.separator + "dir.gzip"), new File(testbase.getCanonicalFile() + File.separator + "out/" ));

            Rest.testSendFile(outfile);



        }catch (Exception e){e.printStackTrace();}


    }
}
