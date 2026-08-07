package com.hackathon.energia_backend.controller;

import com.hackathon.energia_backend.dto.request.RegistroRequest;
import com.hackathon.energia_backend.dto.response.RegistroResponse;
import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.security.JwtUtil;
import com.hackathon.energia_backend.service.AutenticacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar la autenticación de usuarios.
 * Centraliza el proceso de validación de credenciales y la generación
 * de tokens JWT para el acceso seguro a los endpoints protegidos de la API.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "1. Autenticación", description = "Endpoints para login y generación de tokens JWT")
public class AuthController {

    // ============================================
    // Inyección de Dependencias
    // Utilidad para la generación y validación de tokens JWT
    // ============================================
    private final JwtUtil jwtUtil;

    @Autowired
    private AutenticacionService autenticacionService;

    // ============================================
    // Constantes de Configuración (Entorno Hackathon)
    // NOTA: En un entorno de producción, estas credenciales
    // deben validarse contra una base de datos con contraseñas hasheadas.
    // ============================================
    private static final String USER = "admin";
    private static final String PASS = "hackathon2026";

    /**
     * Valida las credenciales del usuario y genera un token de acceso.
     *
     * @param request Objeto DTO que contiene el nombre de usuario y la contraseña.
     * @return {@link ResponseEntity} con el token JWT generado (200 OK) o
     *         un mensaje de error de autorización (401 Unauthorized).
     */
    // ============================================
    // Endpoint: Iniciar Sesión (Login)
    // Método: POST
    // Ruta: /api/auth/login
    // Respuesta: 200 OK (con token) o 401 Unauthorized
    // ============================================
    @Operation(
            summary = "Iniciar sesión y obtener token JWT",
            description = "Envía el usuario y la contraseña para recibir un token de acceso válido."
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {

        // 1. Validación de credenciales contra las constantes configuradas
        var user = autenticacionService.loadUserByUsername(usuario.getUsername(), usuario.getPassword());
//        if (!USER.equals(request.getUsername()) || !PASS.equals(request.getPassword())) {
//            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
//        }

        // 2. Generación del token JWT para el usuario autenticado
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());

        // 3. Retorno de la respuesta exitosa envuelta en el DTO de respuesta
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea una cuenta encriptando la contraseña con BCrypt y devuelve los datos del usuario registrado."
    )
    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o usuario ya registrado")
    @PostMapping("/register")
    public ResponseEntity<RegistroResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        Usuario usuario = autenticacionService.registrarUsuario(request);

        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getId());

        RegistroResponse response = RegistroResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .mensaje("Usuario registrado exitosamente")
                .token(token)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================
    // Data Transfer Objects (DTOs) Internos
    // Modelos de datos específicos para este controlador
    // ============================================

//    /**
//     * DTO para la solicitud de inicio de sesión.
//     */
//    @Data
//    public static class LoginRequest {
//        private String username;
//        private String password;
//    }

    /**
     * DTO para la respuesta de inicio de sesión exitosa.
     */
    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
    }
}