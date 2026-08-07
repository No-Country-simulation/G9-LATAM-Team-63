package com.hackathon.energia_backend.controller;

import com.hackathon.energia_backend.dto.request.AnalisisRequest;
import com.hackathon.energia_backend.dto.response.AnalisisHistorialResponse;
import com.hackathon.energia_backend.dto.response.AnalisisResponse;
import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.service.AnalisisEnergiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas con el análisis de consumo energético.
 * Centraliza la recepción de peticiones HTTP, la validación de datos de entrada y la documentación de la API.
 */
@RestController
@RequestMapping("/api/analisis")
@RequiredArgsConstructor
@Tag(name = "2. Análisis Energético", description = "Endpoints para analizar y consultar consumo")
public class AnalisisEnergiaController {

    // ============================================
    // Inyección de Dependencias
    // Servicio que contiene la lógica de negocio
    // para el procesamiento de análisis energéticos
    // ============================================
    private final AnalisisEnergiaService service;

    /**
     * Procesa y crea un nuevo análisis energético.
     *
     * @param request Objeto de transferencia de datos (DTO) con la información del análisis.
     *                La anotación {@code @Valid} garantiza el cumplimiento de las reglas de validación.
     * @param usuario Usuario autenticado que realiza el análisis, inyectado por Spring Security
     *                desde el {@code SecurityContext} establecido por el {@code JwtAuthenticationFilter}.
     * @return {@link AnalisisResponse} que contiene los resultados del análisis procesado.
     */
    // ============================================
    // Endpoint: Crear Análisis Energético
    // Método: POST
    // Ruta: /api/analisis
    // Respuesta: 201 Created
    // ============================================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Realizar análisis energético")
    @ApiResponse(responseCode = "201", description = "Análisis creado exitosamente")
    public AnalisisResponse crearAnalisis(@Valid @RequestBody AnalisisRequest request,
                                          @AuthenticationPrincipal Usuario usuario) {
        // Delega la validación y procesamiento al servicio de negocio,
        // asociando el análisis al usuario autenticado
        return service.realizarAnalisis(request, usuario);
    }

    /**
     * Consulta el historial completo de análisis del usuario autenticado,
     * ordenado del más reciente al más antiguo.
     *
     * @param usuario Usuario autenticado cuyo historial se consulta.
     * @return Lista de {@link AnalisisHistorialResponse} con los análisis del usuario.
     */
    // ============================================
    // Endpoint: Listar Historial del Usuario
    // Método: GET
    // Ruta: /api/analisis/historial
    // Respuesta: 200 OK
    // Nota: Spring MVC prioriza la ruta literal "/historial"
    // sobre el template "/{id}", evitando conflictos.
    // ============================================
    @GetMapping("/historial")
    @Operation(summary = "Consultar historial de análisis del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Historial del usuario obtenido exitosamente")
    public List<AnalisisHistorialResponse> listarHistorial(@AuthenticationPrincipal Usuario usuario) {
        return service.listarHistorial(usuario);
    }

    /**
     * Consulta los detalles de un análisis energético existente a partir de su identificador único,
     * verificando que pertenezca al usuario autenticado.
     *
     * @param id      Identificador único del análisis a consultar, extraído de la ruta de la petición.
     * @param usuario Usuario autenticado que realiza la consulta.
     * @return {@link ResponseEntity} que envuelve el {@link AnalisisHistorialResponse}
     *         con un estado HTTP 200 (OK).
     */
    // ============================================
    // Endpoint: Consultar Análisis por ID
    // Método: GET
    // Ruta: /api/analisis/{id}
    // Respuesta: 200 OK o 403 si no pertenece al usuario
    // ============================================
    @GetMapping("/{id}")
    @Operation(summary = "Consultar análisis por ID")
    @ApiResponse(responseCode = "200", description = "Análisis encontrado")
    public ResponseEntity<AnalisisHistorialResponse> consultarAnalisis(@PathVariable Long id,
                                                                       @AuthenticationPrincipal Usuario usuario) {
        // Envuelve la respuesta en ResponseEntity para controlar explícitamente el estado HTTP
        return ResponseEntity.ok(service.consultarPorId(id, usuario));
    }
}