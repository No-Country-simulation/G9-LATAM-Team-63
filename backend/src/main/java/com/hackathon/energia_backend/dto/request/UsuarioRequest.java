package com.hackathon.energia_backend.dto.request;

import com.hackathon.energia_backend.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Set;

// ====================================================================
// DTO para la creación de un nuevo usuario con roles asignados.
// ====================================================================
/**
* <p>
 * Centraliza las reglas de validación para garantizar que el username,
 * la contraseña y al menos un rol estén presentes antes de llegar a la capa de negocio.
 * </p>
 */
@Data
public class UsuarioRequest {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // =========================================================================
    // FIX: Cambiado de Set<String> a Set<Rol>
    // @NotNull  → rechaza null
    // @NotEmpty → rechaza [] (array vacío)
    // Jackson valida automáticamente que cada valor sea un Rol válido
    // =========================================================================
    @NotNull(message = "El campo roles es obligatorio. Debe elegir entre: ADMIN, USER, MODERATOR")
    @NotEmpty(message = "El campo roles es obligatorio. Debe elegir entre: ADMIN, USER, MODERATOR")
    private Set<Rol> roles;
}