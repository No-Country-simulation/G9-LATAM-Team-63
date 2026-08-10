import joblib
import numpy as np
import pandas as pd

from ..core.config import settings
from ..models.schemas import AnalisisRequest, AnalisisResponse, TipoInmueble


class PredictionService:
    def __init__(self):
        self.model = None
        self.scaler = None
        self.preprocessor = None
        self.label_encoder = None
        self.feature_names = None
        self._load_artifacts()
    
    def _load_artifacts(self):
        self.model = joblib.load(settings.MODEL_FILE)
        self.scaler = joblib.load(settings.SCALER_FILE)
        self.preprocessor = joblib.load(settings.PREPROCESSOR_FILE)
        self.label_encoder = joblib.load(settings.LABEL_ENCODER_FILE)
        self.feature_names = joblib.load(settings.FEATURE_NAMES_FILE)
        print("Artefactos cargados correctamente.")
    
    def _preprocess(self, data: dict) -> np.ndarray:
        # Orden esperado de columnas
        expected_order = [
            'consumo_kwh', 'cantidad_equipos', 'horas_alto_consumo',
            'tipo_inmueble', 'uso_horario_pico', 'numero_habitantes',
            'antiguedad_inmueble', 'calefaccion', 'aire_acondicionado'
        ]
        df = pd.DataFrame([data])[expected_order]
        
        # Codificar variables categóricas
        encoded = self.preprocessor.transform(df)
        
        # Escalar variables numéricas (últimas 5 columnas)
        num_cols = ['consumo_kwh', 'cantidad_equipos', 'horas_alto_consumo',
                    'numero_habitantes', 'antiguedad_inmueble']
        num_indices = [-len(num_cols) + i for i in range(len(num_cols))]
        encoded[:, num_indices] = self.scaler.transform(encoded[:, num_indices])
        
        return encoded
    
    def _get_recomendaciones(self, categoria: str, request: AnalisisRequest) -> list[str]:
        recomendaciones = []
        ratio = request.consumo_kwh / request.cantidad_equipos if request.cantidad_equipos > 0 else 0
        
        if categoria == "Ineficiente":
            recomendaciones.append("Reducir el uso de equipos durante los horarios pico.")
            recomendaciones.append("Evaluar equipos con alto consumo energético.")
            if request.uso_horario_pico:
                recomendaciones.append("Desplazar actividades de alto consumo a horarios no pico.")
            if request.calefaccion or request.aire_acondicionado:
                recomendaciones.append("Revisar la eficiencia de los sistemas de climatización.")

        elif categoria == "Eficiente":
            recomendaciones.append("Mantener las prácticas actuales de consumo.")
            recomendaciones.append("Monitorear periódicamente el consumo para detectar desviaciones.")

        else:  # Moderado
            recomendaciones.append("Identificar oportunidades de ahorro en horarios pico.")
            if ratio > 40:
                recomendaciones.append("Revisar equipos con mayor consumo por unidad.")
            if request.tipo_inmueble == TipoInmueble.OFICINA:
                recomendaciones.append("Implementar apagado automático de equipos al cierre de jornada.")
            if request.cantidad_equipos > 10:
                recomendaciones.append("Considerar reemplazo por equipos con certificación energética.")
        
        return recomendaciones
    
    def predict(self, request: AnalisisRequest) -> AnalisisResponse:
        # Preprocesar
        X = self._preprocess(request.model_dump())
        
        # Predecir
        pred_encoded = self.model.predict(X)[0]
        proba = self.model.predict_proba(X)[0]
        
        # Decodificar
        categoria = self.label_encoder.inverse_transform([pred_encoded])[0]
        
        # Costo estimado
        costo_estimado = request.consumo_kwh * settings.TARIFA_KWH
        
        # Recomendaciones
        recomendaciones = self._get_recomendaciones(categoria, request)
        
        # Construir respuesta
        return AnalisisResponse(
            categoria=categoria,
            probabilidad=float(proba[pred_encoded]),
            costo_estimado_mensual=costo_estimado,
            recomendaciones=recomendaciones,
            distancias={
                cls: float(proba[i]) for i, cls in enumerate(self.label_encoder.classes_)
            }
        )

# Instancia global
prediction_service = PredictionService()