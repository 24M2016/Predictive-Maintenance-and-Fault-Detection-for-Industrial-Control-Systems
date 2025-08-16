package com.app;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Dashboard extends Application {
    private XYChart.Series<Number, Number> series = new XYChart.Series<>();
    private AtomicInteger timeStep = new AtomicInteger(0);
    private BlockingQueue<Double> rulQueue = new LinkedBlockingQueue<>();
    private ControllerSimulator simulator;
    private Label speedLabel;

    @Override
    public void start(Stage stage) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Real-Time RUL Predictions");
        series.setName("RUL Over Time");
        chart.getData().add(series);

        speedLabel = new Label("Current Speed: 100.0");

        VBox vbox = new VBox(chart, speedLabel);
        Scene scene = new Scene(vbox, 800, 600);
        stage.setScene(scene);
        stage.show();

        // Start simulation in thread
        PredictionService service = new PredictionService();
        simulator = new ControllerSimulator(service, rulQueue);
        new Thread(() -> {
            try {
                simulator.runSimulation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Update chart thread
        new Thread(() -> {
            while (true) {
                try {
                    double rul = rulQueue.take();
                    Platform.runLater(() -> {
                        series.getData().add(new XYChart.Data<>(timeStep.incrementAndGet(), rul));
                        speedLabel.setText("Current Speed: " + simulator.getCurrentSpeed());
                        if (rul < 50) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Failure Alert");
                            alert.setContentText("Predicted RUL below 50! Maintenance recommended.");
                            alert.show();
                        }
                        // Limit data points
                        if (series.getData().size() > 100) {
                            series.getData().remove(0);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}