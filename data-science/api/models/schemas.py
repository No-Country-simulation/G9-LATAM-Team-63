from enum import Enum

from pydantic import BaseModel, Field, field_validator


class TipoInmueble(str, Enum):
    CASA        = "Casa"
    APARTAMENTO = "Apartamento"
    LOCAL       = "Local"
    OFICINA     = "Oficina"


class AnalisisRequest(BaseModel):
    consumo_kwh         : float = Field(..., gt=0, description="Consumo mensual en kWh (rango esperado: 80-1500)")
    cantidad_equipos    : int = Field(..., ge=1, le=50, description="Número de equipos (rango esperado: 2-25)")
    horas_alto_consumo  : int = Field(..., ge=0, le=24, description="Horas de alto consumo (rango esperado: 2-16)")
    tipo_inmueble       : TipoInmueble = Field(..., description="Tipo de inmueble")
    uso_horario_pico    : bool = Field(..., description="Uso en horario pico")
    numero_habitantes   : int = Field(..., ge=1, le=10, description="Número de habitantes")
    antiguedad_inmueble : int = Field(..., ge=0, le=100, description="Antigüedad en años")
    calefaccion         : bool = Field(..., description="Tiene calefacción eléctrica")
    aire_acondicionado  : bool = Field(..., description="Tiene aire acondicionado")

    @field_validator("tipo_inmueble")
    @classmethod
    def validar_tipo(cls, v: TipoInmueble) -> TipoInmueble:
        if v not in list(TipoInmueble):
            raise ValueError(f"Tipo inválido. Opciones: {[e.value for e in TipoInmueble]}")
        return v

    @field_validator("consumo_kwh")
    @classmethod
    def validar_consumo(cls, v: float) -> float:
        if v < 80 or v > 1500:
            raise ValueError(f"Consumo fuera de rango esperado (80-1500 kWh). Valor recibido: {v}")
        return v

    @field_validator("cantidad_equipos")
    @classmethod
    def validar_equipos(cls, v: int) -> int:
        if v < 2 or v > 25:
            raise ValueError(f"Cantidad de equipos fuera de rango esperado (2-25). Valor recibido: {v}")
        return v

    @field_validator("horas_alto_consumo")
    @classmethod
    def validar_horas(cls, v: int) -> int:
        if v < 2 or v > 16:
            raise ValueError(f"Horas de alto consumo fuera de rango esperado (2-16). Valor recibido: {v}")
        return v


class AnalisisResponse(BaseModel):
    categoria               : str = Field(..., description="Categoría de eficiencia energética")
    probabilidad            : float = Field(..., description="Probabilidad asociada a la categoría")
    costo_estimado_mensual  : float = Field(..., description="Costo estimado en USD")
    recomendaciones         : list[str] = Field(default_factory=list, description="Lista de recomendaciones")
    distancias              : dict[str, float] = Field(..., description="Probabilidades por clase")