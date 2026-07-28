
package com.hackathon.energia_backend.dto.request;

/**  permite usar "etiquetas" o anotaciones mágicas encima de tus variables para validar que los
 datos sean correctos antes de procesarlos.*/
import jakarta.validation.constraints.*;

import lombok.Data;
/**
 * DTO (Data Transfer Object) para recibir los datos del análisis.
 * @Data de Lombok genera automáticamente getters, setters, toString, equals y hashCode.
 * Las anotaciones de jakarta.validation validan la entrada antes de llegar al controlador.
 */

@Data
public class AnalisisRequest {

    @NotNull(message = "El consumo en kWh es obligatorio")
    @DecimalMin(value = "0.1", message = "El consumo debe ser mayor a 0")
    private Double consumoKwh;

    @NotNull(message = "El uso de horario pico es obligatorio")
    private Boolean usoHorarioPico;

    @NotNull(message = "La cantidad de equipos es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 equipo")
    @Max(value = 50, message = "La cantidad máxima de equipos es 50")
    private Integer cantidadEquipos;

    @NotNull(message = "El tipo de inmueble es obligatorio")
    @Pattern(regexp = "^(Casa|Apartamento|Local|Oficina)$",
            message = "El tipo debe ser Casa, Apartamento, Local u Oficina")
    private String tipoInmueble;

    @NotNull(message = "Las horas de alto consumo son obligatorias")
    @Min(value = 0, message = "Las horas no pueden ser negativas")
    @Max(value = 24, message = "Las horas no pueden exceder 24")
    private Integer horasAltoConsumo;
}