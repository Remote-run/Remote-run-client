package org.example.util;

import org.example.Packager;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {

    public static void zipDirectory(File dirPath, String zippedDirPath, ZipOutputStream stream) throws IOException {
        zippedDirPath = zippedDirPath.endsWith("/") ? zippedDirPath : zippedDirPath + "/";

        stream.putNextEntry(new ZipEntry(zippedDirPath));
        stream.closeEntry();


        for (File childFile : dirPath.listFiles()) {
            File childFp = new File(dirPath.getAbsolutePath() + "/" + childFile.getName());
            if (childFile.isDirectory()){
                Zipper.zipDirectory(childFp, zippedDirPath + childFile.getName() + "/", stream );
            } else {
                Zipper.zipFile(childFp, zippedDirPath + childFile.getName(), stream);
            }
        }
    }


    public static void zipFile(File filePath, String zippedPath, ZipOutputStream stream) throws IOException{
        FileInputStream inputStream = new FileInputStream(filePath);
        stream.putNextEntry(new ZipEntry(zippedPath));

        byte[] bytes = new byte[1024];
        int  bytes_read;
        while ((bytes_read = inputStream.read(bytes)) >= 0) {
            stream.write(bytes, 0, bytes_read);
        }
        inputStream.close();
    }

    public static void decompressFile(){

    }

    public void gzipFileCompress(File toCompress, File outFile) throws FileNotFoundException, IOException {
        FileInputStream inputStream = new FileInputStream(toCompress);
        FileOutputStream outputStream = new FileOutputStream(outFile);
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);

        byte[] buffer = new byte[4096];
        int bytes_read;
        while ((bytes_read = inputStream.read(buffer)) != -1) {
            gzipOutputStream.write(buffer, 0, bytes_read);
        }

        inputStream.close();
        gzipOutputStream.close();
        outputStream.close();
    }

    public void gzipFileDecompress(File toCompress, File outFile){

    }


}
