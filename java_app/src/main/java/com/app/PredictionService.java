package com.app;


import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class PredictionService {
    private static final String BASE_URL = "http://localhost:5000";  // Or ml_api:5000 in Docker
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();

    public double getRULPredictionRF(double[] features) throws IOException {
        JSONObject json = new JSONObject();
        JSONArray dataArray = new JSONArray(features);
        json.put("data", dataArray);
        return postRequest("/predict_rul_rf", json);
    }

    public boolean detectAnomaly(double[] features) throws IOException {
        JSONObject json = new JSONObject();
        JSONArray dataArray = new JSONArray(features);
        json.put("data", dataArray);
        double result = postRequest("/detect_anomaly", json);
        return result == 1.0;  // Assuming API returns 1 for anomaly (adjust based on -1==true)
    }

    public double getRULPredictionLSTM(double[][] sequence) throws IOException {
        JSONObject json = new JSONObject();
        JSONArray seqArray = new JSONArray();
        for (double[] row : sequence) {
            seqArray.put(new JSONArray(row));
        }
        json.put("sequence", seqArray);
        return postRequest("/predict_rul_lstm", json);
    }

    private double postRequest(String endpoint, JSONObject json) throws IOException {
        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder().url(BASE_URL + endpoint).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JSONObject respJson = new JSONObject(response.body().string());
            if (endpoint.contains("anomaly")) {
                return respJson.getBoolean("anomaly") ? 1.0 : 0.0;
            } else {
                return respJson.getDouble("rul");
            }
        }
    }
}