package com.hackathon.energia_backend.entity;

import com.hackathon.energia_backend.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

//===========================================================================================
// * Entidad JPA que representa la tabla de persistencia para el usuario.
//===========================================================================================

/**
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

    // ============================================
    // Colección de roles del usuario
    // Se almacena en tabla separada (usuario_roles)
    // FetchType.EAGER garantiza que los roles carguen
    // junto con el usuario (necesario para Spring Security)
    // ============================================
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();

    // =========================================================================
    // NUEVO CAMPO: rol_usuario en la tabla usuarios
    // Se almacena como VARCHAR(50) en la BD.
    // nullable = false requiere que la migración Flyway incluya DEFAULT 'USER'.
    // =========================================================================
    @Enumerated(EnumType.STRING)
    @Column(name = "rol_usuario", nullable = false)
    private Rol rolUsuario;

    // =========================================================================
    // Callback JPA: fuerza el username a MAYÚSCULAS antes de insertar/actualizar.
    // Se ejecuta automáticamente por el EntityManager; no requiere llamada manual.
    // =========================================================================
    @PrePersist
    @PreUpdate
    public void preGuardar() {
        if (this.username != null) {
            this.username = this.username.toUpperCase();
        }
    }
}

