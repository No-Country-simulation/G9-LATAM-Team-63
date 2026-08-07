package com.hackathon.energia_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para representar un análisis energético completo en el historial.
 *
 * Combina los parámetros de entrada del análisis (persistidos en la entidad)
 * con los resultados calculados, permitiendo al frontend reconstruir la
 * vista de detalle y la tabla de historial sin depender de almacenamiento local.
 *
 * @JsonProperty asegura que las claves del JSON coincidan con el contrato del frontend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalisisHistorialResponse {

    private Long idAnalisis;

    // ============================================
    // Parámetros de Entrada del Análisis
    // ============================================
    private Double consumoKwh;
    private Boolean usoHorarioPico;
    private Integer cantidadEquipos;
    private String tipoInmueble;
    private Integer numeroHabitantes;
    private Integer antiguedadInmueble;
    private Boolean calefaccion;
    private Boolean aireAcondicionado;
    private Integer horasAltoConsumo;

    // ============================================
    // Resultados del Análisis
    // ============================================
    private String categoria;
    private Double probabilidad;
    private List<String> recomendaciones;

    @JsonProperty("costo_estimado_mensual")
    private Double costoEstimadoMensual;

    @JsonProperty("fecha_creacion")
    private LocalDateTime fechaCreacion;
}
