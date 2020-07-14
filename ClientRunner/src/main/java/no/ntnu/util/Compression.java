package no.ntnu.util;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.zip.*;

/*
TODO:   lots of stuff to do here.
            1. doble check every exeption that gets thrown her what can be handled and are there any plase that wold benefit from a comment in the exeption
            2. zipping files shold have an option to owerwrite or abc(2).zip the files
                2.5. maby add functionallyty to unzip a abc(2).zip -> abc not abc(2)
            3. document everything




 */




/**
 * Utillety class for compressing and decompressing files and dirs
 */
public class Compression {

    private static DebugLogger dbl = new DebugLogger(true);


    private static final File systemTmpDir = new File(System.getProperty("java.io.tmpdir"));

    private static File getFirstFreeName(File wishedName, String endTag) throws IOException{

        File freeName;
        if (wishedName.exists()){
            int increment = 0;

            String maybePath = wishedName.getCanonicalPath() + "(%u)" + endTag;
            while (new File(String.format(maybePath,increment)).exists()){
                increment ++;
            }
            freeName = new File(String.format(maybePath,increment));
        }else {
            freeName = wishedName;
        }

        return freeName;
    }


    /**
     * Zips the file/dir at the given file path to a file
     * @param filePath
     * @throws IOException
     */
    public static File zip(File filePath) throws IOException{
        String maybePath = filePath.getCanonicalPath() + "%s.zip";
        String increment = "";
        while (new File(String.format(maybePath,increment)).exists()){
            increment = (increment.equals(""))? "1" : String.valueOf(Integer.parseInt(increment) + 1);
        }
        File outFile = new File(String.format(maybePath,increment));
        Compression.zip(filePath, outFile);
        return outFile;
    }

    public static void zip(File filePath, File outPath) throws IOException{
        if (outPath.exists()) throw new FileAlreadyExistsException("Zip file alredy exists");
        if (!filePath.exists()) throw new IOException("Zip target not found");


        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outPath));
        zipOutputStream.setLevel(Deflater.NO_COMPRESSION); // using the zip as a tar
        if (filePath.isDirectory()){

            Compression.zipDirectory(filePath, filePath.getName(), zipOutputStream);

        } else if (filePath.isFile()){
            Compression.zipFile(filePath, filePath.getName(), zipOutputStream);
        }
        zipOutputStream.close();
    }

    public static void zip(File filePath, ZipOutputStream zipOutputStream, String zippedDirCurrentPath) throws IOException{
        if (!filePath.exists()) throw new IOException("Zip target not found");

        if (filePath.isDirectory()){
            Compression.zipDirectory(filePath, zippedDirCurrentPath, zipOutputStream);

        } else if (filePath.isFile()){
            Compression.zipFile(filePath, zippedDirCurrentPath, zipOutputStream);
        }
    }



    public static void gZip(File filePath) throws IOException{
        Compression.gZip(filePath, null);
    }

    public static void gZip(File filePath, File outPath) throws IOException{
        outPath = (outPath == null) ? new File(filePath.getCanonicalPath() + ".gzip"): outPath;
        //if (outPath.exists()) throw new FileAlreadyExistsException("Zip file alredy exists");
        if (!filePath.exists()) throw new IOException("Zip target not found");



        File compressTarget = filePath;
        if (filePath.isDirectory()){
            File tmpZipFile = new File(Compression.systemTmpDir, filePath.getName());
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(tmpZipFile));
            zipOutputStream.setLevel(Deflater.NO_COMPRESSION); // using the zip as a tar
            Compression.zipDirectory(filePath, tmpZipFile.getName(), zipOutputStream);
            zipOutputStream.close();
            compressTarget = tmpZipFile;
        }

        Compression.gzipFileCompress(compressTarget, outPath);
    }

    public static void unzip(File filePath) throws IOException{
        Compression.unzip(filePath, null);
    }

    public static void unzip(File filePath, File outDir) throws IOException{
        if (outDir == null){
            dbl.log("no out dir given");
            outDir = Compression.getPathWithoutEnding(filePath);
            outDir.mkdir();
        }


        if (filePath.getName().endsWith(".gzip") || filePath.getName().endsWith(".gz")){
            dbl.log("gunzip detected starting unzip");

            File tmpZipFile = new File(Compression.systemTmpDir.getCanonicalPath() + File.separator  + filePath.getName().substring(0,".gzip".length() + 1) );
            //tmpZipFile.delete();
            //File tmpZipFile = new File(Compression.systemTmpDir().getCanonicalPath() + File.separator  + filePath.getName());
            gzipFileDecompress(filePath, tmpZipFile);
            _unzip(tmpZipFile, outDir);
            tmpZipFile.delete();
            dbl.log("unzip complete");
        }else {
            dbl.log("normal zip decompress");
            _unzip(filePath, outDir);
        }
    }

    // -- private
    private static File getPathWithoutEnding(File file) throws  IOException{
        return new File(file.getCanonicalPath().substring(0, file.getCanonicalPath().lastIndexOf(".")));

    }

    private static void zipDirectory(File dirPath, String zippedDirPath, ZipOutputStream stream) throws IOException {
        zippedDirPath = zippedDirPath.endsWith(File.separator ) ? zippedDirPath : zippedDirPath + File.separator ;

        stream.putNextEntry(new ZipEntry(zippedDirPath));
        stream.closeEntry();


        for (File childFile : dirPath.listFiles()) {
            File childFp = new File(dirPath.getCanonicalPath() + File.separator + childFile.getName());
            if (childFile.isDirectory()){
                Compression.zipDirectory(childFp, zippedDirPath + childFile.getName() + File.separator , stream );
            } else {
                Compression.zipFile(childFp, zippedDirPath + childFile.getName(), stream);
            }
        }
    }


    private static void zipFile(File filePath, String zippedPath, ZipOutputStream stream) throws IOException{
        FileInputStream inputStream = new FileInputStream(filePath);
        stream.putNextEntry(new ZipEntry(zippedPath));

        byte[] bytes = new byte[1024];
        int  bytes_read;
        while ((bytes_read = inputStream.read(bytes)) >= 0) {
            stream.write(bytes, 0, bytes_read);
        }
        inputStream.close();
        stream.closeEntry();
    }

    private static void _unzip(File filePath, File outDir) throws IOException {
        if (!filePath.exists()) throw new IOException("unzip target not found");

        dbl.log("Outfile");
        dbl.fileLog(outDir);

        byte[] buffer = new byte[1024];

        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filePath));
        ZipEntry zipEntry = zipInputStream.getNextEntry();

        while (zipEntry != null) {
            File entryFile = new File(outDir, zipEntry.getName());
            testForZipSlip(entryFile,outDir);
            if (zipEntry.isDirectory()){
                entryFile.mkdir();
            } else {
                FileOutputStream outputStream = new FileOutputStream(entryFile);

                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.close();
            }

            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.closeEntry();
        zipInputStream.close();
    }

    private static void testForZipSlip(File entryPath, File outDirPath) throws IOException{
        if (!entryPath.getCanonicalPath().startsWith(outDirPath.getCanonicalPath() + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + entryPath.getCanonicalPath());
        }
    }

    private static void gzipFileCompress(File toCompress, File outFile) throws FileNotFoundException, IOException {
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

    private static void gzipFileDecompress(File compressed, File outFile) throws  FileNotFoundException, IOException{
        FileInputStream inputStream = new FileInputStream(compressed);
        FileOutputStream outputStream = new FileOutputStream(outFile);
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

        byte[] buffer = new byte[4096];
        int bytes_read;
        while ((bytes_read = gzipInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytes_read);
        }

        inputStream.close();
        gzipInputStream.close();
        outputStream.close();
    }


}
