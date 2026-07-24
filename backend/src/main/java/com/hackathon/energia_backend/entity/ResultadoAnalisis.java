package com.hackathon.energia_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa la tabla en la base de datos.
 * @Entity indica que es una tabla, @Table define su nombre.
 */
@Entity
@Table(name = "resultados_analisis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoAnalisis {

    /** Le dice a la base de datos: "Este es el campo principal, la Clave Primaria. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental
    private Long id; // Long se usa cuando son numeros enteros grandes

    @Column(nullable = false) // define la columna en la tabla y no puede estar vacio

    private Double consumoKwh;

    @Column(nullable = false)
    private Boolean usoHorarioPico;

    @Column(nullable = false)
    private Integer cantidadEquipos;

    @Column(nullable = false)
    private String tipoInmueble;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Double probabilidad;

    @Column(nullable = false)
    private Double costoEstimadoMensual;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // @PrePersist se ejecuta automáticamente antes de guardar el registro en la BD
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
