from fastapi import APIRouter, HTTPException, status

from ..models.schemas import AnalisisRequest, AnalisisResponse
from ..services.prediction_service import prediction_service

router = APIRouter(prefix="/api/v1/predict", tags=["Predicción"])


@router.post("/", response_model=AnalisisResponse, status_code=status.HTTP_200_OK)
async def predict(request: AnalisisRequest):
    try:
        response = prediction_service.predict(request)
        return response
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"Error de validación: {e}"
        )
    except FileNotFoundError as e:
        raise HTTPException(
            status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
            detail=f"Error de configuración del servicio: {e}"
        )
