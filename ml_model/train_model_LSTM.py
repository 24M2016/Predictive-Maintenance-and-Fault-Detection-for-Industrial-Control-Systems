
import numpy as np
from sklearn.model_selection import train_test_split
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense
from tensorflow.keras.utils import to_categorical  # If needed

# Reshape for LSTM: (samples, timesteps, features)
# Assume we window data into sequences of 50 cycles
def create_sequences(data, seq_length=50):
    xs, ys = [], []
    for engine in data['engine_id'].unique():
        engine_data = data[data['engine_id'] == engine].sort_values('cycle')
        for i in range(len(engine_data) - seq_length):
            x = engine_data[features].iloc[i:i+seq_length].values
            y = engine_data['rul'].iloc[i+seq_length]
            xs.append(x)
            ys.append(y)
    return np.array(xs), np.array(ys)

X_seq, y_seq = create_sequences(data)
X_train_seq, X_test_seq, y_train_seq, y_test_seq = train_test_split(X_seq, y_seq, test_size=0.2)

lstm_model = Sequential()
lstm_model.add(LSTM(50, input_shape=(X_train_seq.shape[1], X_train_seq.shape[2])))
lstm_model.add(Dense(1))
lstm_model.compile(optimizer='adam', loss='mse')
lstm_model.fit(X_train_seq, y_train_seq, epochs=20, batch_size=32)
lstm_model.save('D:/Akhilesh_python_ws/Predictive-Maintenance-and-Fault-Detection-for-Industrial-Control-Systems/ml_model/models/lstm_model.h5')