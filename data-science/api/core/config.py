import os


class Settings:
    # Rutas
    BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    MODELS_DIR = os.path.join(BASE_DIR, '..', 'models', 'saved')
    
    # Archivos de modelo y preprocesadores
    MODEL_FILE = os.path.join(MODELS_DIR, 'xgboost_model.pkl')
    SCALER_FILE = os.path.join(MODELS_DIR, 'scaler.pkl')
    PREPROCESSOR_FILE = os.path.join(MODELS_DIR, 'preprocessor.pkl')
    LABEL_ENCODER_FILE = os.path.join(MODELS_DIR, 'label_encoder.pkl')
    FEATURE_NAMES_FILE = os.path.join(MODELS_DIR, 'feature_names.pkl')
    
    # Tarifa por kWh (para estimación de costo)
    TARIFA_KWH = 0.75
    
settings = Settings()