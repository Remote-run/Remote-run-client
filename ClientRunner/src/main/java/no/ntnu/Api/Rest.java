package no.ntnu.Api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import java.io.File;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;


public class Rest {

    public static void testSendFile(File sendFile) throws IOException {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            final HttpPost httppost = new HttpPost("http://localhost:8080" +
                    "/com.example.RemoteRunApiServlet/");

            final FileBody gZipfile = new FileBody(sendFile);
            final StringBody comment = new StringBody("Gunzip file", ContentType.TEXT_PLAIN);

            final HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("gzip", gZipfile)
                    .addPart("comment", comment)
                    .build();


            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost);
            try (final CloseableHttpResponse response = httpclient.execute(httppost)) {
                System.out.println("----------------------------------------");
                System.out.println(response);
                final HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            }
        }
        /*HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/RemoteRunApiServlet/").openConnection();
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
        }*/
    }
}
