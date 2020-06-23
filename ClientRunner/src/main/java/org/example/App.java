package org.example;

import org.example.util.Compression;

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
            File testUncompressedDir = new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/toziptestsak");
            File testUncompressedFile = new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/abc");

            File testbase = new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/");

            System.out.println(testbase.getCanonicalPath());
            Compression.gZip(testUncompressedFile, new File(testbase.getCanonicalPath() + File.separator + "fil.gzip"));
            Compression.gZip(testUncompressedDir, new File(testbase.getCanonicalPath() + File.separator + "dir.gzip"));

            Compression.zip(testUncompressedFile, new File(testbase.getCanonicalPath() + File.separator + "fil.zip"));
            Compression.zip(testUncompressedDir, new File(testbase.getCanonicalPath() + File.separator + "dir.zip"));

            Compression.unzip(new File(testbase.getCanonicalPath() + File.separator + "fil.gzip"), new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/out"));
            Compression.unzip(new File(testbase.getCanonicalPath() + File.separator + "dir.gzip"), new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/out"));

            Compression.unzip(new File(testbase.getCanonicalPath() + File.separator + "fil.zip"), new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/out"));
            Compression.unzip(new File(testbase.getCanonicalPath() + File.separator + "dir.zip"), new File("/home/trygve/tmp/RemoteProj_testing/ziptesting/out"));

        }catch (Exception e){e.printStackTrace();}


    }
}
