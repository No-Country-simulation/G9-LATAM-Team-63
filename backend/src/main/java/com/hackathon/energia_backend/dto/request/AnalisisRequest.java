package com.hackathon.energia_backend.dto.request;

// ============================================
// Importaciones de Validación de Datos
// Jakarta Validation proporciona restricciones
// para validar la entrada antes de que llegue a la lógica de negocio
// ============================================
import jakarta.validation.constraints.*;
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
    //@NotNull(message = "El consumo en kWh es obligatorio")
    //@DecimalMin(value = "0.1", message = "El consumo debe ser mayor a 0")
    //private Double consumoKwh;
    @NotNull(message = "El consumo en kWh es obligatorio")
    @DecimalMin(value = "80", message = "El consumo mínimo permitido es 80 kWh")
    @DecimalMax(value = "1500", message = "El consumo máximo permitido es 1500 kWh")
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
    @Min(value = 2, message = "Debe haber al menos 2 equipo")
    @Max(value = 25, message = "La cantidad máxima de equipos es 25")
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
    @Min(value = 2, message = "Las horas no pueden ser menores a 2")
    @Max(value = 16, message = "Las horas no pueden exceder 16")
    private Integer horasAltoConsumo;

    // ============================================
    // Campo: Cantidad de habitantes en el inmueble
    // Reglas: No nulo, rango válido entre 1 y 6 personas
    // ============================================
    @NotNull(message = "La cantidad de habitantes es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 habitante")
    @Max(value = 6, message = "La cantidad máxima de habitantes es 6")
    private Integer numeroHabitantes;

    // ============================================
    // Campo: Años de antigüedad de la construcción
    // Reglas: No nulo, rango válido entre 0 y 50 años
    // ============================================
    @NotNull(message = "La cantidad de años es obligatoria")
    @Min(value = 0, message = "No se puede ingresar un valor menor que 0")
    @Max(value = 50, message = "La cantidad máxima de años es 50")
    private Integer antiguedadInmueble;

    // ============================================
    // Campo: Indica si el inmueble cuenta con calefacción eléctrica
    // Reglas: No nulo, debe de indicarse con true o false
    // ============================================
    @NotNull(message = "La especificación de la calefacción es obligatoria")
    private Boolean calefaccion;

    // ============================================
    // Campo: Indica si el inmueble cuenta con aire acondicionado
    // Reglas: No nulo, debe de indicarse con true o false
    // ============================================
    @NotNull(message = "La especificación del aire acondicionado es obligatoria")
    private Boolean aireAcondicionado;
}