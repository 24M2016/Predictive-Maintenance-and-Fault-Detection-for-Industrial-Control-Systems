import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
import datetime
import os

def load_data(file_path):
    columns = ['engine_id', 'cycle'] + [f'op_setting_{i}' for i in range(1, 4)] + [f'sensor_{i}' for i in range(1, 22)]
    data = pd.read_csv(file_path, sep=' ', header=None, usecols=range(26), names=columns)
    return data

def add_rul(data):
    # Calculate RUL: max_cycle - current_cycle for each engine
    max_cycles = data.groupby('engine_id')['cycle'].max().reset_index()
    max_cycles.columns = ['engine_id', 'max_cycle']
    data = data.merge(max_cycles, on='engine_id')
    data['rul'] = data['max_cycle'] - data['cycle']
    data.drop('max_cycle', axis=1, inplace=True)
    # Clip RUL at 130 (common practice for early degradation)
    data['rul'] = np.minimum(data['rul'], 130)
    return data

def preprocess(data):
    # Drop irrelevant columns (e.g., constant sensors)
    drop_cols = ['op_setting_3', 'sensor_1', 'sensor_5', 'sensor_6', 'sensor_10', 'sensor_16', 'sensor_18', 'sensor_19']  # Based on variance analysis
    data = data.drop(drop_cols, axis=1)
    # Normalize sensors
    scaler = MinMaxScaler()
    sensor_cols = [col for col in data.columns if 'sensor' in col]
    data[sensor_cols] = scaler.fit_transform(data[sensor_cols])
    return data, scaler

def generate_file_with_time_extension():
    now = datetime.datetime.now()
    extension = f"{now.second:02d}{int(now.microsecond / 100000)}"
    return extension




# Example usage
if __name__ == '__main__':
    training_data_path='D:/Akhilesh_python_ws/Predictive-Maintenance-and-Fault-Detection-for-Industrial-Control-Systems/dataset/data/CMAPSSData/Turbofan_Engine_Degradation_Simulation_Data_Set/train_FD001.txt'

    train_data = load_data(training_data_path)
    train_data = add_rul(train_data)
    train_data, scaler = preprocess(train_data)
    train_data.to_csv('D:/Akhilesh_python_ws/Predictive-Maintenance-and-Fault-Detection-for-Industrial-Control-Systems/dataset/data/Processed_Dataset/processed_train.csv', index=False)
    print("Preprocessing done!")