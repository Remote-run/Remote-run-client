package no.ntnu.Api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Rest {

    public static void testSendFile(File sendFile) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/RemoteRunApiServlet/").openConnection();
        System.out.println(1);
        connection.setRequestMethod("POST");


        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        FileInputStream inputStream = new FileInputStream(sendFile);
        System.out.println(2);

        System.out.println(connection.getResponseMessage());

        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        System.out.println(3);
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        System.out.println(4);
        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);
        if(responseCode == 200){
            System.out.println("POST was successful.");
        }
        else if(responseCode == 401){
            System.out.println("Wrong password.");
        }
    }
}
