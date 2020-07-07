package no.ntnu.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * The DebugLogger class allows for a quick, easy and toggelable debug print interface.
 */
public class DebugLogger {

    private boolean print;

    /**
     * Makes a debug logger
     * @param print whether or not the logger should print
     */
    public DebugLogger(boolean print) {
        this.print = print;
    }

    /**
     * prints arbitrary list of objects using the objects toString method
     * the location where the print statement where printed from is also shown.
     * @param tolog the objects to log
     */
    public void log(Object ...tolog){
        if (this.print) {
            StackTraceElement tracePos = this.getCallerStackPoisson();

            String printString = Stream.of(tolog).map(Objects::toString).collect(Collectors.joining());
            System.out.printf("%-70s",printString);

            if (tracePos != null){
                System.out.printf("\u001B[32m\t\t%s.%s\u001B[0m(\u001B[36m%s:%d\u001B[0m)\n",
                        tracePos.getClassName(),
                        tracePos.getMethodName(),
                        tracePos.getFileName(),
                        tracePos.getLineNumber());
            } else {
                System.out.println();
            }
        }
    }

    /**
     * A more specilized loggging for files where the file name, parent, type and so on is shown.
     * @param file the file to debug
     */
    public void fileLog(File file){
        try {
            System.out.println("-- Debug file log --");
            System.out.printf(
                    "File name      : %s\n" +
                    "File parent    : %s\n" +
                    "Caonical path  : %s\n", file.getName(), file.getParent(), file.getCanonicalPath());
            if (!file.exists()) {
                System.out.println("- file dont exist -");
            }else if(file.isDirectory()){
                System.out.printf(
                    "- file is directory -\n" +
                    "Num children    : %s\n", file.listFiles().length);
            }else if(file.isDirectory()){
                System.out.printf(
                    "- file is file -" +
                    "Size             : %s\n", file.length());
            }

            System.out.println("-- log end --");
        } catch (IOException e){
            System.out.println("debug err IO exeption");
        }
    }


    /**
     * Returns the first stack trace element outside this class.
     * Used to find where the log methode is called from
     * @return the first stack trace element outside this class.
     */
    private StackTraceElement getCallerStackPoisson(){
        for (StackTraceElement stackTraceElement: Thread.currentThread().getStackTrace()) {
            if (!stackTraceElement.getClassName().equals(this.getClass().getName())
                    && !stackTraceElement.getMethodName().equals("getStackTrace")) {
                return  stackTraceElement;
            }
        }
        return null;
    }


}
