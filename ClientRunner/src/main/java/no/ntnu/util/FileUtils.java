package no.ntnu.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static final File executionDir = new File("").getAbsoluteFile();

    /**
     * Cheks whther or not the given file is a child of the given directory.
     * if ether is null false is returned
     * @param file The file to check if is in the directory
     * @param parent The dir to check if the file is in
     * @return true if the file is in any of the dirs sub directorys
     */
    public static boolean isFileChildOfDir(File file, File parent){
        boolean isChild = false;
        try{
            if (file.exists() && parent.exists()){
                if (file.getCanonicalPath().startsWith(parent.getCanonicalPath())){
                     isChild = true;
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        return isChild;
    }

    /**
     * Deletes a directory recursivly, symbolic links are not followed
     * @param file The dir to delete.
     */
    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }
}
