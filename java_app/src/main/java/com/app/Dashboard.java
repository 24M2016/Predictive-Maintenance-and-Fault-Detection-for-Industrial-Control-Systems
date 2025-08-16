package com.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard extends Application {
    @Override
    public void start(Stage stage) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Real-Time RUL Predictions");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("RUL Over Time");
        // Add data dynamically from simulation

        VBox vbox = new VBox(chart);
        Scene scene = new Scene(vbox, 800, 600);
        stage.setScene(scene);
        stage.show();

        // Thread to update chart from simulation
        new Thread(() -> {
            // Integrate with ControllerSimulator to add data points
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}