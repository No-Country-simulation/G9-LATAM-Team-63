package com.hackathon.energia_backend.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO para la respuesta del análisis.
 * @Builder permite crear el objeto de forma fluida (builder pattern).
 * @JsonProperty asegura que las claves del JSON coincidan exactamente con el brief.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalisisResponse {

    private String categoria;
    private Double probabilidad;
    private List<String> recomendaciones;

    // El brief pide "costo_estimado_mensual" con guion bajo
    @JsonProperty("costo_estimado_mensual")
    private Double costoEstimadoMensual;

    private Long idAnalisis;
}