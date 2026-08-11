package com.hackathon.energia_backend.controller;

import com.hackathon.energia_backend.dto.request.UsuarioRequest;
import com.hackathon.energia_backend.dto.response.UsuarioResponse;
import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.enums.Rol;
import com.hackathon.energia_backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ================================================================================================
 * Controlador REST para la gestión de usuarios y sus permisos.
 *
 * Fix aplicado (Simplificación de roles):
 *   - Eliminado MODERATOR de todas las expresiones SpEL.
 *   - listarUsuarios() ahora admite hasAnyRole('ADMIN', 'USER').
 * ================================================================================================
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(
        name = "3. Gestión de Usuarios",
        description = "Endpoints para crear y administrar usuarios con roles y permisos"
)
@SecurityRequirement(name = "Bearer Authentication")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea un nuevo usuario en el sistema con roles específicos.
     *
     * Seguridad: Solo usuarios con rol ADMIN pueden ejecutar esta operación.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Crear nuevo usuario con roles",
            description = "Registra un usuario con permisos específicos. Requiere rol ADMIN."
    )
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMIN")
    @ApiResponse(responseCode = "409", description = "El username ya existe")
    public UsuarioResponse crearUsuario(@Valid @RequestBody UsuarioRequest request) {

        // Validación de negocio: username único
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El username ya está registrado");
        }

        // =========================================================================
        // FIX 1: Obtener roles del DTO (ya convertidos a Set<Rol> por Jackson)
        // =========================================================================
        Set<Rol> roles = request.getRoles();

        // =========================================================================
        // FIX 2: Extraer el rol principal para el campo obligatorio rol_usuario.
        // Se toma el primer elemento del Set. Si viene vacío (improbable por @NotEmpty
        // en el DTO), se usa USER como valor por defecto.
        // =========================================================================
        Rol rolPrincipal = roles.isEmpty() ? Rol.USER : roles.iterator().next();

        // =========================================================================
        // FIX 3: Construcción de la entidad con Builder.
        // Se agrega .rolUsuario(rolPrincipal) porque la entidad JPA marca este campo
        // como nullable = false. Sin este valor, Hibernate lanza:
        // "Column 'rol_usuario' cannot be null" al hacer INSERT.
        // =========================================================================
        Usuario nuevo = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .rolUsuario(rolPrincipal)   // ← NUEVO: campo obligatorio en la entidad
                .build();

        Usuario guardado = usuarioRepository.save(nuevo);

        return UsuarioResponse.builder()
                .id(guardado.getId())
                .username(guardado.getUsername())
                .roles(guardado.getRoles().stream().map(Rol::name).collect(Collectors.toSet()))
                .build();
    }

    public record ErrorResponse(String error) {}

    /**
     * Lista todos los usuarios registrados en el sistema.
     *
     * Seguridad: Accesible para roles ADMIN y USER.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "Listar usuarios",
            description = "Retorna el listado completo de usuarios registrados."
    )
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> UsuarioResponse.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .roles(u.getRoles().stream().map(Rol::name).collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());
    }

    // =========================================================================
    // NUEVO ENDPOINT: Eliminar usuario (solo ADMIN)
    // =========================================================================
    @Operation(summary = "🗑️ Eliminar Usuario", description = "Elimina un usuario por su ID. Requiere rol ADMIN.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMIN")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(
            @Parameter(description = "ID del usuario a eliminar", required = true)
            @PathVariable Long id
    ) {

        // Verificar que el usuario exista antes de borrar
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado con ID: " + id));
        }

        usuarioRepository.deleteById(id);

        return ResponseEntity.noContent().build(); // 204 No Content
    }
}