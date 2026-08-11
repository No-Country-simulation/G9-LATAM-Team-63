from fastapi import FastAPI, Request, status
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse

from .routers import predict

# Crear la aplicación FastAPI
app = FastAPI(
    title="EnergiAI - API de Predicción",
    description="API para clasificar eficiencia energética y generar recomendaciones de ahorro",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc"
)

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Manejador global de excepciones
@app.exception_handler(Exception)
async def global_exception_handler(request: Request, exc: Exception):
    return JSONResponse(
        status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
        content={"detail": f"Error interno del servidor: {exc!s}"}
    )

# Incluir las rutas
app.include_router(predict.router)

# Endpoint raíz
@app.get("/", tags=["Root"])
async def root():
    return {
        "message": "EnergiAI API - Predicción de Eficiencia Energética",
        "docs": "/docs",
        "redoc": "/redoc"
    }