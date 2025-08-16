package com.app;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class PredictionService {
    private static final String API_URL = "http://localhost:5000/predict_rul";

    public double getRULPrediction(double[] features) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        json.put("data", features);
        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder().url(API_URL).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            JSONObject respJson = new JSONObject(response.body().string());
            return respJson.getDouble("rul");
        }
    }

    // Similar methods for /detect_anomaly and /predict_lstm
}