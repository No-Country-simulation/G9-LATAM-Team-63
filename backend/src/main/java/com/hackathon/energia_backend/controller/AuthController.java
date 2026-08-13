package com.hackathon.energia_backend.controller;

import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.enums.Rol;
import com.hackathon.energia_backend.repository.UsuarioRepository;
import com.hackathon.energia_backend.security.JwtUtil;
import com.hackathon.energia_backend.service.AutenticacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Login y registro de usuarios")
public class AuthController {

    // ==========================================
    // Dependencias de seguridad y persistencia
    // Inyección de utilidades JWT, servicio de auth y repositorio de usuarios
    // ==========================================
    private final JwtUtil jwtUtil;
    private final AutenticacionService autenticacionService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // ==========================================
    // Endpoint de autenticación
    // Valida credenciales y genera token JWT de sesión
    // ==========================================
    /**
     * Autentica un usuario y devuelve un token JWT.
     */
    @Operation(summary = "Iniciar sesión")
    @ApiResponse(responseCode = "200", description = "Token JWT generado")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UserDetails user = autenticacionService.authenticate(
                request.getUsername(),
                request.getPassword()
        );
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    // ==========================================
    // Endpoint de registro de usuarios
    // Crea cuenta nueva, asigna rol por defecto y retorna JWT
    // ==========================================
    /**
     * Registra un nuevo usuario, asigna rol por defecto y devuelve un JWT.
     */
    @Operation(summary = "Crear cuenta")
    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente")
    @ApiResponse(responseCode = "409", description = "El nombre de usuario ya existe")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("El nombre de usuario ya está en uso"));
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Las contraseñas no coinciden"));
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRolUsuario(Rol.USER);

        usuarioRepository.save(usuario);

        UserDetails user = autenticacionService.authenticate(
                request.getUsername(),
                request.getPassword()
        );
        String token = jwtUtil.generateToken(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterResponse(token, "Cuenta creada exitosamente"));
    }

    // ==========================================
    // DTOs internos del controlador
    // Clases de transferencia para peticiones y respuestas de auth
    // ==========================================

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String confirmPassword;
    }

    @Data
    @AllArgsConstructor
    public static class RegisterResponse {
        private String token;
        private String message;
    }

    @Data
    @AllArgsConstructor
    public static class MessageResponse {
        private String message;
    }
}