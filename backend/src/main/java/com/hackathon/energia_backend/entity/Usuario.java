package com.hackathon.energia_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa la tabla de persistencia para el usuario.
 *
 * Esta clase mapea el modelo de dominio a la estructura relacional de la base de datos,
 * almacenando los parámetros de entrada del usuario.
 *
 * Anotaciones de Lombok utilizadas:
 * <ul>
 *   <li>{@code @Data}: Genera getters, setters, toString, equals y hashCode.</li>
 *   <li>{@code @Builder}: Implementa el patrón Builder para una construcción fluida de la entidad.</li>
 *   <li>{@code @NoArgsConstructor} y {@code @AllArgsConstructor}: Requeridos por JPA y el Builder.</li>
 * </ul>
 */
@Entity(name = "Usuario")
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JoinColumn(name = "username")
    private String username;

    @Column(nullable = false)
    @JoinColumn(name = "password")
    private String password;
}
