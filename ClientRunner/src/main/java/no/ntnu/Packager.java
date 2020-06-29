package no.ntnu;

import no.ntnu.config.ApiConfig;
import no.ntnu.util.Compression;

import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.ZipOutputStream;

public  class Packager {
    private static final File tmpDir = new File(System.getProperty("java.io.tmpdir"));

    /**
     * Gunzip's the files specified in the provided config and puts them
     * in the system tmp dir
     * @param config the config dict
     * @return A file object pointing to the packaged file
     */
    public static File packageDir(ApiConfig config)throws FileNotFoundException, IOException {
        File zipFileName = new File(Packager.tmpDir + "/remote_run.zip");
        File gzipFileName = new File(Packager.tmpDir + "/remote_run.gzip");
        FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        zipOutputStream.setLevel(Deflater.NO_COMPRESSION);

        config.getPackingList()
                .forEach(file -> {
                    try {
                        Compression.zip(file, zipOutputStream, file.getName());
                    } catch (IOException ignored){}
                });

        zipOutputStream.close();
        fileOutputStream.close();

        Compression.gZip(zipFileName,gzipFileName);

        return gzipFileName;

    }








}
