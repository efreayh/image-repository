package com.example.imagerepository.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class LabellingService {

    @Value("${imaggaApiKey}")
    private String imaggaAPIKey;
    @Value("${imaggaApiSecret}")
    private String imaggaAPISecret;
    private String imaggaAPIEndpoint;

    @Autowired
    public LabellingService() {
        imaggaAPIEndpoint = "https://api.imagga.com/v2";
    }

    public JSONObject getLabels(String uploadId) throws IOException {

        String credentialsToEncode = imaggaAPIKey + ":" + imaggaAPISecret;
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        URL url = new URL(imaggaAPIEndpoint + "/tags" + "?image_upload_id=" + uploadId);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        JSONObject response = new JSONObject(connectionInput.readLine());

        connectionInput.close();
        connection.disconnect();

        return response;
    }

    public JSONObject uploadFile(MultipartFile file) throws IOException {

        String credentialsToEncode = imaggaAPIKey + ":" + imaggaAPISecret;
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        URL url = new URL(imaggaAPIEndpoint + "/uploads");
        HttpURLConnection connection = createUploadConnection(url, basicAuth);

        makeUploadRequest(connection, file);

        JSONObject response = getUploadResponse(connection);

        connection.disconnect();

        return response;
    }

    private HttpURLConnection createUploadConnection(URL url, String basicAuth) throws IOException {
        String boundary =  "Image Upload";

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        return connection;
    }

    private void makeUploadRequest(HttpURLConnection connection, MultipartFile file) throws IOException {
        String boundary =  "Image Upload";
        String crlf = "\r\n";
        String twoHyphens = "--";

        InputStream inputStream = file.getInputStream();
        DataOutputStream request = new DataOutputStream(connection.getOutputStream());

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + file.getOriginalFilename() + "\"" + crlf);
        request.writeBytes(crlf);

        int bytesRead = 0;
        byte[] dataBuffer = new byte[1024];

        while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
            request.write(dataBuffer, 0, bytesRead);
        }

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
        request.flush();
        request.close();
        inputStream.close();
    }

    private JSONObject getUploadResponse(HttpURLConnection connection) throws IOException {
        InputStream responseStream = new BufferedInputStream(connection.getInputStream());

        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        responseStreamReader.close();
        responseStream.close();

        return new JSONObject(stringBuilder.toString());
    }
}