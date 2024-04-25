package com.example.facebook_iso.api_manager;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.facebook_iso.Login_Page;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class api_manager {

    private final OkHttpClient client;
    private final Context context;


    public api_manager(Context context) {
        this.client = new OkHttpClient();
        this.context = context;
    }

    //    public String post(String endpoint, Map<String, Object> parameters) throws IOException {
//        System.out.println("Debug123: Creating URL...");
//        CompletableFuture<Boolean> future = new CompletableFuture<>();
//        StringBuilder response = new StringBuilder();
//        AsyncTask.execute(() -> {
//
//            try{
//                URL url = new URL(constants.baseUrl + endpoint);
//
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//                connection.setRequestProperty("Accept", "application/json");
//                connection.setRequestProperty("Content-Type", "application/json");
//
//                connection.setRequestMethod("POST");
//
//                connection.setDoOutput(true);
//                connection.setDoInput(true);
//
//                connection.connect();
//                String postData = mapToJson(parameters);
//
//                byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
//
//                OutputStream outputStream = connection.getOutputStream();
//
//                outputStream.write(postDataBytes);
//
//                outputStream.flush();
//
//                outputStream.close();
//
//                int status = connection.getResponseCode();
//                if(status == 200) {
//                    InputStream inputStream = connection.getInputStream();
//                    System.out.println("Debug123: Creating reader...");
//                    InputStreamReader inputStreamBuilder = new InputStreamReader(inputStream);
//                    System.out.println("Debug123: Reader created...");
//                    BufferedReader reader = new BufferedReader(inputStreamBuilder);
//                    System.out.println("Debug123: Reader created");
//
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    reader.close();
//                    connection.disconnect();
//                } else{
//                    InputStreamReader isr = new InputStreamReader(connection.getErrorStream());
//                    System.out.println("Debug123: " + connection.getResponseCode());
//                    System.out.println("Debug123: " + connection.getResponseMessage());
//                }
//            }
//            catch (Exception e)
//            {
//                System.out.println("Debug123: " + e.toString());
//            }
//
//            future.complete(true);
//        });
//        return response.toString();
//    }


    public Future<Response> post(String endpoint, Map<String, Object> parameters) {
        CompletableFuture<Response> future = new CompletableFuture<>();

        AsyncTask.execute(() -> {
            try {

                Request request = new Request.Builder()
                        .url(constants.baseUrl + endpoint)
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mapToJson(parameters)))
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    future.complete(response);
                } catch (IOException e) {
                    System.out.println("Debug123: Error executing request: " + e.getMessage());
                    e.printStackTrace();
                    future.completeExceptionally(e);
                }
            } catch (Exception e) {
                System.out.println("Debug123: Error: " + e.getMessage());
                e.printStackTrace();
                future.completeExceptionally(e);
            }
        });


        return future;
    }


    public Future<Response> get(String endpoint, String bearerToken) {
        CompletableFuture<Response> future = new CompletableFuture<>();

        AsyncTask.execute(() -> {
            try {

                Request.Builder requestBuilder = new Request.Builder()
                        .url(constants.baseUrl + endpoint)
                        .get()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");


                if (bearerToken != null && !bearerToken.isEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer " + bearerToken);
                }

                Request request = requestBuilder.build();
                try {
                    Response response = client.newCall(request).execute();
                    future.complete(response);
                } catch (IOException e) {
                    System.out.println("Debug123: Error executing request: " + e.getMessage());
                    e.printStackTrace();
                    future.completeExceptionally(e);
                }
            } catch (Exception e) {
                System.out.println("Debug123: Error: " + e.getMessage());
                e.printStackTrace();
                future.completeExceptionally(e);
            }
        });


        return future;
    }
//    public Future<String> get(String endpoint) {
//        CompletableFuture<String> future = new CompletableFuture<>();
//
//        AsyncTask.execute(() -> {
//            try {
//                URL url = new URL(constants.baseUrl + endpoint);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder responseBuilder = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    responseBuilder.append(line);
//                }
//                reader.close();
//
//                connection.disconnect();
//
//                future.complete(responseBuilder.toString());
//            } catch (IOException e) {
//                System.out.println("Debug123: Error executing request: " + e.getMessage());
//                e.printStackTrace();
//                future.completeExceptionally(e);
//            } catch (Exception e) {
//                System.out.println("Debug123: Error: " + e.getMessage());
//                e.printStackTrace();
//                future.completeExceptionally(e);
//            }
//        });
//
//        return future;
//    }

    public String multiPost(String endpoint, Map<String, Object> parameters, File file) throws IOException {
        URL url = new URL(constants.baseUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        String postData = mapToJson(parameters);
        byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
        connection.getOutputStream().write(postDataBytes);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        connection.disconnect();

        return response.toString();
    }

    private String mapToJson(Map<String, Object> map) {
        StringBuilder postData = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            postData.append("\"").append(entry.getKey()).append("\":\"");
            if (entry.getValue() instanceof String) {
                postData.append(entry.getValue()).append("\"");
            } else {
                postData.append(entry.getValue().toString()).append("\"");
            }
            postData.append(",");
        }
        if (postData.length() > 1) {
            postData.deleteCharAt(postData.length() - 1);
        }
        postData.append("}");

        return postData.toString();
    }
}
