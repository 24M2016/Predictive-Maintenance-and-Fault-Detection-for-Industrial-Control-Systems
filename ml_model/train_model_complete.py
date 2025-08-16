# ml_model/train_model.py

import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor, IsolationForest
from sklearn.metrics import mean_squared_error
import joblib
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense
from tensorflow.keras.callbacks import EarlyStopping

def create_sequences(data, features, seq_length=50):
    xs, ys = [], []
    for engine in data['engine_id'].unique():
        engine_data = data[data['engine_id'] == engine].sort_values('cycle')
        for i in range(len(engine_data) - seq_length):
            x = engine_data[features].iloc[i:i+seq_length].values
            y = engine_data['rul'].iloc[i+seq_length]
            xs.append(x)
            ys.append(y)
    return np.array(xs), np.array(ys)

# Load processed data
data = pd.read_csv('../data/processed_train.csv')
features = [col for col in data.columns if col not in ['engine_id', 'cycle', 'rul']]
X = data[features]
y = data['rul']

# Split for supervised models
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Train RUL Predictor (Random Forest)
rf_model = RandomForestRegressor(n_estimators=100, random_state=42)
rf_model.fit(X_train, y_train)
prediction = rf_model.predict(X_test)
root_mean_square_errors = np.sqrt(mean_squared_error(y_test, prediction))
print(f"RUL RootMeanSquareError (RF): {root_mean_square_errors}")

# Train Anomaly Detector (Isolation Forest)
iso_model = IsolationForest(contamination=0.01, random_state=42)
iso_model.fit(X_train)

# Prepare sequences for LSTM
X_seq, y_seq = create_sequences(data, features)
X_train_seq, X_test_seq, y_train_seq, y_test_seq = train_test_split(X_seq, y_seq, test_size=0.2, random_state=42)

# Train LSTM
lstm_model = Sequential()
lstm_model.add(LSTM(50, input_shape=(X_train_seq.shape[1], X_train_seq.shape[2]), return_sequences=True))
lstm_model.add(LSTM(50))
lstm_model.add(Dense(1))
lstm_model.compile(optimizer='adam', loss='mse')
early_stop = EarlyStopping(monitor='val_loss', patience=5)
lstm_model.fit(X_train_seq, y_train_seq, epochs=50, batch_size=32, validation_split=0.1, callbacks=[early_stop])
lstm_root_mean_square_error = np.sqrt(mean_squared_error(y_test_seq, lstm_model.predict(X_test_seq)))
print(f"RUL RMSE (LSTM): {lstm_root_mean_square_error}")

# Save models
joblib.dump(rf_model, 'models/rul_rf_model.pkl')
joblib.dump(iso_model, 'models/anomaly_model.pkl')
lstm_model.save('models/lstm_model.h5')
print("Models trained and saved!")