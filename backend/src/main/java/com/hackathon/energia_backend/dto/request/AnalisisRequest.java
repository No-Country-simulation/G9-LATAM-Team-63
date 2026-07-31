package com.hackathon.energia_backend.dto.request;

// ============================================
// Importaciones de Validación de Datos
// Jakarta Validation proporciona restricciones
// para validar la entrada antes de que llegue a la lógica de negocio
// ============================================
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO (Data Transfer Object) para la recepción de datos de análisis energético.
 *
 * Esta clase encapsula los datos de entrada y aplica reglas de validación
 * en la capa de presentación, garantizando la integridad de los datos antes
 * de que sean procesados por los servicios de la aplicación.
 *
 * La anotación {@code @Data} de Lombok genera automáticamente getters, setters,
 * toString, equals y hashCode, reduciendo el código boilerplate.
 */
@Data
public class AnalisisRequest {

    // ============================================
    // Campo: Consumo Energético
    // Reglas: No nulo y estrictamente mayor a cero
    // ============================================
    @NotNull(message = "El consumo en kWh es obligatorio")
    @DecimalMin(value = "0.1", message = "El consumo debe ser mayor a 0")
    private Double consumoKwh;

    // ============================================
    // Campo: Indicador de Horario Pico
    // Reglas: No nulo (requiere valor explícito true/false)
    // ============================================
    @NotNull(message = "El uso de horario pico es obligatorio")
    private Boolean usoHorarioPico;

    // ============================================
    // Campo: Cantidad de Equipos
    // Reglas: No nulo, rango válido entre 1 y 50 equipos
    // ============================================
    @NotNull(message = "La cantidad de equipos es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 equipo")
    @Max(value = 50, message = "La cantidad máxima de equipos es 50")
    private Integer cantidadEquipos;

    // ============================================
    // Campo: Tipo de Inmueble
    // Reglas: No nulo y debe coincidir exactamente con uno de los valores permitidos
    // Nota: Se usa Pattern como alternativa ligera a un Enum para validación de strings
    // ============================================
    @NotNull(message = "El tipo de inmueble es obligatorio")
    @Pattern(regexp = "^(Casa|Apartamento|Local|Oficina)$",
            message = "El tipo debe ser Casa, Apartamento, Local u Oficina")
    private String tipoInmueble;

    // ============================================
    // Campo: Horas de Alto Consumo
    // Reglas: No nulo, rango válido entre 0 y 24 horas (límite físico del día)
    // ============================================
    @NotNull(message = "Las horas de alto consumo son obligatorias")
    @Min(value = 0, message = "Las horas no pueden ser negativas")
    @Max(value = 24, message = "Las horas no pueden exceder 24")
    private Integer horasAltoConsumo;
}