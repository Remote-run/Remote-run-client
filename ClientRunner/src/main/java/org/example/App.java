package org.example;

import org.example.util.ClassPath;
import org.example.util.Meta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ArrayList<ClassPath> ret = new ArrayList(Arrays.asList(Meta.probeForClassPath(new File("/home/trygve/dev/projects/PredatorPreySimulation/src"))));
        ret.forEach(classPath -> System.out.println(classPath.asDotFormat()));

    }
}
