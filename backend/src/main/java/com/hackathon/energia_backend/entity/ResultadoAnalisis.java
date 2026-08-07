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
@Entity(name = "ResultadoAnalisis")
@Table(name = "analisis_energetico")
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
    @JoinColumn(name = "consumo_kwh")
    private Double consumoKwh;

    // ============================================
    // Horas de Alto Consumo
    // Dato de entrada utilizado por el motor de reglas para
    // clasificar la categoría. nullable = true para no romper
    // los registros previos a la existencia de esta columna.
    // ============================================
    @Column(nullable = true)
    @JoinColumn(name = "horas_alto_consumo")
    private Integer horasAltoConsumo;

    @Column(nullable = false)
    @JoinColumn(name = "uso_horario_pico")
    private Boolean usoHorarioPico;

    @Column(nullable = false)
    @JoinColumn(name = "cantidad_equipos")
    private Integer cantidadEquipos;

    @Column(nullable = false)
    @JoinColumn(name = "tipo_inmueble")
    private String tipoInmueble;

    @Column(nullable = false)
    @JoinColumn(name = "numero_habitantes")
    private Integer numeroHabitantes;

    @Column(nullable = false)
    @JoinColumn(name = "antiguedad_inmueble")
    private Integer antiguedadInmueble;

    @Column(nullable = false)
    @JoinColumn(name = "calefaccion")
    private Boolean calefaccion;

    @Column(nullable = false)
    @JoinColumn(name = "aire_acondicionado")
    private Boolean aireAcondicionado;

    // ============================================
    // Resultados del Análisis
    // Datos calculados por el motor de reglas o modelo de predicción.
    // ============================================
    @Column(nullable = false)
    @JoinColumn(name = "categoria")
    private String categoria;

    @Column(nullable = false)
    @JoinColumn(name = "probabilidad")
    private Double probabilidad;

    @Column(nullable = false)
    @JoinColumn(name = "costo_estimado_mensual")
    private Double costoEstimadoMensual;

    // ============================================
    // Campos de Auditoría
    // Registro inmutable de la fecha y hora de creación del análisis.
    // updatable = false previene modificaciones accidentales en operaciones UPDATE.
    // ============================================
    @Column(nullable = false, updatable = false)
    @JoinColumn(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // ============================================
    // Relación con el Usuario Propietario
    // Asocia cada análisis al usuario autenticado que lo generó,
    // permitiendo consultar el historial por usuario.
    // nullable (por defecto en @JoinColumn) para no romper
    // los registros creados antes de existir la relación.
    // ============================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

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