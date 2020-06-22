package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

public  class Packager {
    private File tmpDir = new File(System.getProperty("java.io.tmpdir"));

    /**
     * Packages the files spesefied in the provided config and puts them
     * in the system tmp dir
     * @param config the config dict
     * @return A file object pointing to the packaged file
     */
    public static File packageDir(HashMap<String,String> config){
        return null;

    }
}
