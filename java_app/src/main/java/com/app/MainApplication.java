package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);
        PredictionService service = new PredictionService();
        ControllerSimulator simulator = new ControllerSimulator(service);
        simulator.runSimulation();  // Run control loop
        // Launch JavaFX Dashboard in separate thread if needed
    }
}