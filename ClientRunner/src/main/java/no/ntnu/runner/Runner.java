package no.ntnu.runner;

import no.ntnu.config.ApiConfig;

import java.io.File;
import java.util.Scanner;

public class Runner {

    protected static boolean isMailValid(ApiConfig config){
        return !config.getReturnMail().contains("@");
    }

    protected static String queryForMail(){
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
