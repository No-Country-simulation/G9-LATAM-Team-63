package com.hackathon.energia_backend.dto.datascience;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // ignora 'distancias' si no la usamos aún
public class DataScienceResponse {

    private String categoria;
    private Double probabilidad;

    @JsonProperty("costo_estimado_mensual")
    private Double costoEstimadoMensual;

    private List<String> recomendaciones;

    // Opcional: si luego quieres exponer las probabilidades por clase al frontend
    @JsonProperty("distancias")
    private Map<String, Double> distancias;
}