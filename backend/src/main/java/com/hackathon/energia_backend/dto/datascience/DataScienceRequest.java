package com.hackathon.energia_backend.dto.datascience;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataScienceRequest {

    @JsonProperty("consumo_kwh")
    private Double consumoKwh;

    @JsonProperty("cantidad_equipos")
    private Integer cantidadEquipos;

    @JsonProperty("horas_alto_consumo")
    private Integer horasAltoConsumo;

    @JsonProperty("tipo_inmueble")
    private String tipoInmueble;

    @JsonProperty("uso_horario_pico")
    private Boolean usoHorarioPico;

    @JsonProperty("numero_habitantes")
    private Integer numeroHabitantes;

    @JsonProperty("antiguedad_inmueble")
    private Integer antiguedadInmueble;

    @JsonProperty("calefaccion")
    private Boolean calefaccion;

    @JsonProperty("aire_acondicionado")
    private Boolean aireAcondicionado;
}