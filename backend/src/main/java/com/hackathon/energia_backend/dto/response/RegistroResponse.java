package com.hackathon.energia_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de registro exitoso de un usuario.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroResponse {

    private Long id;
    private String username;
    private String mensaje;
    private String token;
}
