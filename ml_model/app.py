# ml_model/app.py

from flask import Flask, request, jsonify
import joblib
import numpy as np
from tensorflow.keras.models import load_model

app = Flask(__name__)
rul_rf_model = joblib.load('models/rul_rf_model.pkl')
anomaly_model = joblib.load('models/anomaly_model.pkl')
lstm_model = load_model('models/lstm_model.h5')

@app.route('/predict_rul_rf', methods=['POST'])
def predict_rul_rf():
    data = request.json['data']  # Expect list of feature values
    data = np.array(data).reshape(1, -1)
    prediction = rul_rf_model.predict(data)[0]
    return jsonify({'rul': float(prediction)})

@app.route('/detect_anomaly', methods=['POST'])
def detect_anomaly():
    data = request.json['data']
    data = np.array(data).reshape(1, -1)
    anomaly = anomaly_model.predict(data)[0]  # -1 for anomaly
    return jsonify({'anomaly': anomaly == -1})

@app.route('/predict_rul_lstm', methods=['POST'])
def predict_rul_lstm():
    seq = request.json['sequence']  # List of lists (timesteps x features)
    seq = np.array(seq).reshape(1, len(seq), len(seq[0]))
    prediction = lstm_model.predict(seq)[0][0]
    return jsonify({'rul': float(prediction)})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)