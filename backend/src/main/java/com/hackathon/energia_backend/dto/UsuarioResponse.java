package com.hackathon.energia_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

// ========================================================================
// * DTO de respuesta para la gestión de usuarios.
// ========================================================================

/**
 * <p>
 * Expone únicamente los datos necesarios al cliente, ocultando información sensible
 * como la contraseña hasheada.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String username;
    private Set<String> roles;
}