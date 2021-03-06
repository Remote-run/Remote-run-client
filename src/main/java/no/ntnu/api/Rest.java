package no.ntnu.api;

import no.trygvejw.http.MultiPartForm;

import java.io.*;


import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/*
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
*/

/**
 * Utility class for making rest api calls
 */
public class Rest {

    /**
     * Makes a Rest post call to the provided url containing a multipart post with the provided file
     * @param sendFile The file to send
     * @param url the url to make the post call to
     * @throws IOException If the provided file does not exist
     */
    /*public static void aapostFile(File sendFile, String url) throws IOException {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            final HttpPost httppost = new HttpPost(url);

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
                    BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent()));

                    reader.lines().forEach(System.out::println);
                    reader.close();
                }
                EntityUtils.consume(resEntity);
            }
        }
    }*/

    public static void postFile(File sendFile, String url) throws IOException {

        HttpClient client = HttpClient.newHttpClient();



        try {

            MultiPartForm multiPartForm = new MultiPartForm().addPart("zip_file", sendFile).addPart("blblab","aa"); // if there is not a feald at the end errors occure

            HttpRequest request = HttpRequest
                    .newBuilder(URI.create(url))
                    .header("Content-Type", multiPartForm.getContentTypeHeader())
                    .timeout(Duration.ofMinutes(1))
                    .POST(multiPartForm.getPublisher())
                    .build();


            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("HTTP response code: " + response.statusCode());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
