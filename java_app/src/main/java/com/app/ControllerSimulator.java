package com.app;

import java.util.Random;

public class ControllerSimulator {
    private double currentSpeed = 100.0;  // Initial motor speed
    private PredictionService predictionService;

    public ControllerSimulator(PredictionService service) {
        this.predictionService = service;
    }

    public void runSimulation() throws Exception {
        Random rand = new Random();
        while (true) {
            // Simulate sensor data (replace with real IoT input)
            double[] features = new double[20];  // Match your features count
            for (int i = 0; i < features.length; i++) {
                features[i] = rand.nextDouble();  // Or load from dataset
            }

            double rul = predictionService.getRULPrediction(features);
            System.out.println("Predicted RUL: " + rul);

            // Feedback: Adjust if failure predicted
            if (rul < 50) {
                currentSpeed *= 0.8;  // Reduce speed by 20%
                System.out.println("Alert: Potential failure! Reduced speed to " + currentSpeed);
            } else {
                currentSpeed = Math.min(100.0, currentSpeed + 5);  // Restore
            }

            Thread.sleep(5000);  // Simulate every 5s
        }
    }
}