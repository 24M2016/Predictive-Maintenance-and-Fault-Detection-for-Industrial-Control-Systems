import joblib
import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestRegressor, IsolationForest
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import train_test_split

# Load processed data
data = pd.read_csv('D:/Akhilesh_python_ws/Predictive-Maintenance-and-Fault-Detection-for-Industrial-Control-Systems/dataset/data/Processed_Dataset/processed_train.csv')
features = [col for col in data.columns if col not in ['engine_id', 'cycle', 'rul']]
X = data[features]
y = data['rul']

# Split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Train RUL Predictor (Supervised)
rf_model = RandomForestRegressor(n_estimators=100, random_state=42)
rf_model.fit(X_train, y_train)
prediction = rf_model.predict(X_test)
rms_error = np.sqrt(mean_squared_error(y_test, prediction))
print(f"RUL RMSE: {rms_error}")
print(f"Prediction: {prediction}")

# Train Anomaly Detector (Unsupervised)
iso_model = IsolationForest(contamination=0.01, random_state=42)
iso_model.fit(X_train)

# Save models
joblib.dump(rf_model, 'D:/Akhilesh_python_ws/Predictive-Maintenance-and-Fault-Detection-for-Industrial-Control-Systems/ml_model/models/rul_model.pkl')
joblib.dump(iso_model, 'D:/Akhilesh_python_ws/Predictive-Maintenance-and-Fault-Detection-for-Industrial-Control-Systems/ml_model/models/anomaly_model.pkl')