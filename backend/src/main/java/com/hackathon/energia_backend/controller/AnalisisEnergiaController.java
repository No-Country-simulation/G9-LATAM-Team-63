// ================================================================
// Controlador REST para operaciones de análisis energético.
// FIX: Se usa Authentication (contexto de Spring Security) en vez de
// @AuthenticationPrincipal UserDetails, porque el JwtAuthenticationFilter
// inyecta el username como String principal, no como objeto UserDetails.
// ================================================================

package com.hackathon.energia_backend.controller;

import com.hackathon.energia_backend.dto.request.AnalisisRequest;
import com.hackathon.energia_backend.dto.response.AnalisisHistorialResponse;
import com.hackathon.energia_backend.dto.response.AnalisisResponse;
import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.repository.UsuarioRepository;
import com.hackathon.energia_backend.service.AnalisisEnergiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analisis")
@RequiredArgsConstructor
@Tag(name = "2. Análisis Energético", description = "Endpoints para analizar y consultar consumo")
public class AnalisisEnergiaController {

    private final AnalisisEnergiaService service;
    private final UsuarioRepository usuarioRepository;

    // ================================================================
    // 1. Crear Análisis Energético
    // POST /api/analisis → 201 Created
    // ================================================================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Realizar análisis energético")
    @ApiResponse(responseCode = "201", description = "Análisis creado exitosamente")
    public AnalisisResponse crearAnalisis(@Valid @RequestBody AnalisisRequest request,
                                          Authentication authentication) {
        Usuario usuario = resolveUsuario(authentication);
        return service.realizarAnalisis(request, usuario);
    }

    // ================================================================
    // 2. Listar Historial del Usuario
    // GET /api/analisis/historial → 200 OK
    // ================================================================
    @GetMapping("/historial")
    @Operation(summary = "Consultar historial de análisis del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Historial del usuario obtenido exitosamente")
    public List<AnalisisHistorialResponse> listarHistorial(Authentication authentication) {
        Usuario usuario = resolveUsuario(authentication);
        return service.listarHistorial(usuario);
    }

    // ================================================================
    // 3. Consultar Análisis por ID
    // GET /api/analisis/{id} → 200 OK | 403 si no pertenece al usuario
    // ================================================================
    @GetMapping("/{id}")
    @Operation(summary = "Consultar análisis por ID")
    @ApiResponse(responseCode = "200", description = "Análisis encontrado")
    public ResponseEntity<AnalisisHistorialResponse> consultarAnalisis(@PathVariable Long id,
                                                                       Authentication authentication) {
        Usuario usuario = resolveUsuario(authentication);
        return ResponseEntity.ok(service.consultarPorId(id, usuario));
    }

    // ================================================================
    // Resuelve la entidad Usuario desde el username del JWT.
    // El filtro JWT guarda el username como principal String;
    // authentication.getName() lo recupera de forma segura.
    // ================================================================
    private Usuario resolveUsuario(Authentication authentication) {
        String username = authentication.getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado en la base de datos"));
    }
}