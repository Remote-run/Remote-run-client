package no.ntnu.runner;

import no.ntnu.config.ApiConfig;

import java.io.File;
import java.util.Scanner;

public class Runner {

    protected File getConfigFIle(){
        File configfile = null;
        try {

            //configfile = new File(ApiConfig.)
        } catch (Exception e){

        }
        return null;
    }

    protected boolean isMailValid(ApiConfig config){
        return !config.getReturnMail().contains("@");
    }

    protected String queryForMail(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--------------------------------");
        System.out.println("    Config");
        System.out.println("--------------------------------");
        System.out.print("Input return mail:");

        String input = scanner.nextLine();
        scanner.close();
        return input;

    }
}
