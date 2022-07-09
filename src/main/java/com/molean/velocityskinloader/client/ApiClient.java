package com.molean.velocityskinloader.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class ApiClient {

    protected final HttpClient httpClient;
    protected final Gson gson;

    @Getter
    protected final String base;


    public ApiClient(String base) {
        this.base = base;
        httpClient = HttpClient
                .newBuilder()
                .build();
        gson = new GsonBuilder().create();
    }

    private HttpRequest.BodyPublisher ofMimeMultipartData(Map<String, Object> data, String boundary) throws IOException {
        List<byte[]> byteArrays = new ArrayList<>();
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=").getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            byteArrays.add(separator);
            if (entry.getValue() instanceof Path path) {
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName()
                        + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n")
                        .getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }

    protected HttpResponse<String> postFormData(String path, Map<String, Object> data) throws Exception {
        String boundary = new BigInteger(256, new Random()).toString();
        HttpRequest build = HttpRequest.newBuilder()
                .uri(URI.create(base + path))
                .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                .POST(ofMimeMultipartData(data, boundary))
                .build();
        HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
        errorHandle(send);
        return send;
    }

    protected HttpResponse<String> postJson(String path, String json) throws Exception {
        HttpRequest build = HttpRequest.newBuilder()
                .uri(URI.create(base + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
        errorHandle(send);
        return send;
    }

    protected HttpResponse<String> get(String path) throws Exception {
        HttpRequest build = HttpRequest.newBuilder()
                .uri(URI.create(base + path))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
        errorHandle(send);
        return send;
    }

    protected HttpResponse<byte[]> getBytes(String path) throws Exception {
        HttpRequest build = HttpRequest.newBuilder()
                .uri(URI.create(base + path))
                .GET()
                .build();
        HttpResponse<byte[]> send = httpClient.send(build, HttpResponse.BodyHandlers.ofByteArray());
        errorHandle(send);
        return send;
    }

    protected void errorHandle(HttpResponse<?> response) throws Exception{
        if (response.statusCode() / 100 != 2) {
            throw new RuntimeException();
        }
    }
}
