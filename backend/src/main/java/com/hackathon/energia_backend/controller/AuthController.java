package com.hackathon.energia_backend.controller;

<<<<<<< HEAD
=======
import com.hackathon.energia_backend.dto.request.RegistroRequest;
import com.hackathon.energia_backend.dto.response.RegistroResponse;
import com.hackathon.energia_backend.entity.Usuario;
>>>>>>> 1bf5e3c056f6c589018e6740dfc590644ddeec8f
import com.hackathon.energia_backend.security.JwtUtil;
import com.hackathon.energia_backend.service.AutenticacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
>>>>>>> 1bf5e3c056f6c589018e6740dfc590644ddeec8f
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

<<<<<<< HEAD
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
=======
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
>>>>>>> 1bf5e3c056f6c589018e6740dfc590644ddeec8f

    //================================================================
    // *DTO interno para la respuesta de inicio de sesión exitosa.
    //================================================================
    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
    }
}