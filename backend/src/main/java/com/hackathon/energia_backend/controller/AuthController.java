package com.hackathon.energia_backend.controller;

import com.hackathon.energia_backend.security.JwtUtil;
import com.hackathon.energia_backend.service.AutenticacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

//================================================================================
// * Controlador REST encargado de gestionar la autenticación de usuarios.
// ================================================================================
/**
 * <p>
 * <strong>Cambio realizado:</strong> El endpoint de login ahora utiliza
 * {@link AutenticacionService#authenticate(String, String)} que retorna un
 * {@link UserDetails} con los roles reales del usuario. El token JWT generado
 * incluye dichos roles para su uso posterior en autorización.
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "1. Autenticación", description = "Endpoints para login y generación de tokens JWT")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AutenticacionService autenticacionService;

    //================================================================================
    // * Valida credenciales y genera un token JWT con los roles del usuario.
    //================================================================================
    /**
     *
     * @param request DTO con username y password.
     * @return Token JWT en un DTO de respuesta.
     */
    @Operation(
            summary = "Iniciar sesión y obtener token JWT",
            description = "Envía el usuario y la contraseña para recibir un token con los roles asignados."
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Autentica contra la BD y obtiene UserDetails con authorities
        UserDetails user = autenticacionService.authenticate(request.getUsername(), request.getPassword());

        // 2. Genera token JWT que incluye los roles en el payload
        String token = jwtUtil.generateToken(user);

        // 3. Retorna respuesta exitosa
        return ResponseEntity.ok(new LoginResponse(token));
    }

    //========================================================
    //DTO interno para la solicitud de inicio de sesión.
    // ========================================================
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    //================================================================
    // *DTO interno para la respuesta de inicio de sesión exitosa.
    //================================================================
    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
    }
}