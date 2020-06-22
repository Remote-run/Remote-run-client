package org.example;

import org.example.enums.RunTypes;
import org.example.util.Zipper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public  class Packager {
    private static File tmpDir = new File(System.getProperty("java.io.tmpdir"));

    /**
     * Gunzip's the files specified in the provided config and puts them
     * in the system tmp dir
     * @param config the config dict
     * @return A file object pointing to the packaged file
     */
    public static File packageDir(APIConfig config)throws FileNotFoundException, IOException {
        String zipFileName = Packager.tmpDir + "/remote_run.zip";
        String gzipFileName = Packager.tmpDir + "/remote_run.gzip";
        FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
        ZipOutputStream stream = new ZipOutputStream(fileOutputStream);
        stream.putNextEntry(new ZipEntry("/config"));
        Zipper

        if (config.getRuntype() == RunTypes.JAVA){
            .add(new File(config.getSrcRoot()));
        } else if (config.getRuntype() == RunTypes.PYTHON){

        }


        return null;

    }








}
