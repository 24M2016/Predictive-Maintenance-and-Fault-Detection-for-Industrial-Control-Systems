
# 🔧 Predictive Maintenance & Fault Detection for Industrial Control Systems

This project implements a machine learning-based system for predictive maintenance and fault detection in industrial environments (e.g., manufacturing, robotics, power systems). It integrates with control systems and includes a Java-based app for real-time monitoring.

---

## Features

- Predict equipment failures using ML (supervised & unsupervised)
- Time-series analysis with sensor data (vibration, temperature, pressure)
- Control system feedback to optimize operation
- Java-based GUI/API (Spring Boot, JavaFX)
- Dockerized deployment for scalability

---

##  Project Structure
predictive-maintenance-control-system/
├── data/ # Sensor datasets (e.g., NASA Turbofan)
├── ml_model/ # ML training & inference
├── control_system/ # Control logic & simulation
├── java_app/ # Java API & GUI (Spring Boot, JavaFX)
├── deployment/ # Docker, CI/CD setup
└── README.md


---

##  Dataset Used

**NASA Turbofan Engine Degradation Simulation Dataset**  
- Source: [NASA PHM Repository](https://www.nasa.gov/content/prognostics-center-of-excellence-data-set-repository)  
- Use Case: Predict Remaining Useful Life (RUL), detect anomalies and failure patterns.

---

##  Tech Stack

- **Languages**: Java, Python  
- **ML Libraries**: scikit-learn, TensorFlow, Weka, Deeplearning4j  
- **Java Frameworks**: Spring Boot, JavaFX  
- **Simulation**: Simulink / OpenModelica  
- **Tools**: Docker, Git, Maven



