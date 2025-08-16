package com.app;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControllerSimulator {
    private double currentSpeed = 100.0;  // Initial motor speed
    private final PredictionService predictionService;
    private final BlockingQueue<Double> rulQueue;  // For GUI updates

    public ControllerSimulator(PredictionService service, BlockingQueue<Double> rulQueue) {
        this.predictionService = service;
        this.rulQueue = rulQueue;
    }

    public void runSimulation() throws Exception {
        Random rand = new Random();
        int featureCount = 20;  // Adjust based on actual features
        int seqLength = 50;  // For LSTM
        double[][] sequence = new double[seqLength][featureCount];

        while (true) {
            // Simulate sensor data (shift sequence for time-series)
            for (int i = 0; i < seqLength - 1; i++) {
                sequence[i] = sequence[i + 1];
            }
            double[] newData = new double[featureCount];
            for (int i = 0; i < featureCount; i++) {
                newData[i] = rand.nextDouble() * 2 - 1;  // Simulated normalized data
            }
            sequence[seqLength - 1] = newData;

            // Get predictions
            double rulRF = predictionService.getRULPredictionRF(newData);
            boolean anomaly = predictionService.detectAnomaly(newData);
            double rulLSTM = predictionService.getRULPredictionLSTM(sequence);

            double rul = (rulRF + rulLSTM) / 2;  // Average for ensemble
            System.out.println("Predicted RUL: " + rul + ", Anomaly: " + anomaly);

            // Feedback control
            if (rul < 50 || anomaly) {
                currentSpeed = Math.max(50.0, currentSpeed * 0.8);  // Reduce speed, min 50
                System.out.println("Alert: Potential failure! Reduced speed to " + currentSpeed);
            } else {
                currentSpeed = Math.min(100.0, currentSpeed + 5);  // Gradually restore
            }

            // Update GUI queue
            rulQueue.put(rul);

            Thread.sleep(2000);  // Simulate every 2 seconds
        }
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }
}