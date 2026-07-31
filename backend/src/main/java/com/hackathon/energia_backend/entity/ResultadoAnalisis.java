package com.hackathon.energia_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la tabla de persistencia para los resultados del análisis energético.
 *
 * Esta clase mapea el modelo de dominio a la estructura relacional de la base de datos,
 * almacenando tanto los parámetros de entrada del análisis como los resultados calculados.
 *
 * Anotaciones de Lombok utilizadas:
 * <ul>
 *   <li>{@code @Data}: Genera getters, setters, toString, equals y hashCode.</li>
 *   <li>{@code @Builder}: Implementa el patrón Builder para una construcción fluida de la entidad.</li>
 *   <li>{@code @NoArgsConstructor} y {@code @AllArgsConstructor}: Requeridos por JPA y el Builder.</li>
 * </ul>
 */
@Entity
@Table(name = "resultados_analisis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoAnalisis {

    // ============================================
    // Clave Primaria (Primary Key)
    // Identificador único del registro.
    // La estrategia IDENTITY delega la generación del ID autoincremental
    // al motor de la base de datos subyacente.
    // ============================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ============================================
    // Parámetros de Entrada del Análisis
    // Datos originales proporcionados por el usuario para el cálculo.
    // La restricción nullable = false garantiza la integridad referencial a nivel de BD.
    // ============================================
    @Column(nullable = false)
    private Double consumoKwh;

    @Column(nullable = false)
    private Boolean usoHorarioPico;

    @Column(nullable = false)
    private Integer cantidadEquipos;

    @Column(nullable = false)
    private String tipoInmueble;

    // ============================================
    // Resultados del Análisis
    // Datos calculados por el motor de reglas o modelo de predicción.
    // ============================================
    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Double probabilidad;

    @Column(nullable = false)
    private Double costoEstimadoMensual;

    // ============================================
    // Campos de Auditoría
    // Registro inmutable de la fecha y hora de creación del análisis.
    // updatable = false previene modificaciones accidentales en operaciones UPDATE.
    // ============================================
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // ============================================
    // Callback del Ciclo de Vida de JPA
    // Se ejecuta automáticamente antes de la operación INSERT (persist).
    // Garantiza que la fecha de creación se establezca de forma centralizada
    // sin depender de la lógica del servicio.
    // ============================================
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}