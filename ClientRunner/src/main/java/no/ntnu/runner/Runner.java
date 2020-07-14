package no.ntnu.runner;

import no.ntnu.config.ApiConfig;

import java.io.File;
import java.util.Scanner;

public abstract class Runner {

    /*

    TODO:     I am like 43% shure that in the OOP subject i made a general cli input handler, if so find it 


     */




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
