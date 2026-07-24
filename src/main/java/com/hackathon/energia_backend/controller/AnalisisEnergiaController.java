package com.hackathon.energia_backend.controller;

import com.hackathon.energia_backend.dto.request.AnalisisRequest;
import com.hackathon.energia_backend.dto.response.AnalisisResponse;
import com.hackathon.energia_backend.service.AnalisisEnergiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas con el análisis de consumo energético.
 * Centraliza la recepción de peticiones HTTP, la validación de datos de entrada y la documentación de la API.
 */
@RestController
@RequestMapping("/api/analisis")
@RequiredArgsConstructor
@Tag(name = "Análisis Energético", description = "Endpoints para analizar y consultar consumo")
public class AnalisisEnergiaController {

    private final AnalisisEnergiaService service;

    /**
     * Procesa y crea un nuevo análisis energético.
     *
     * @param request Objeto de transferencia de datos (DTO) con la información del análisis.
     *                La anotación {@code @Valid} garantiza el cumplimiento de las reglas de validación definidas en el DTO.
     * @return {@link AnalisisResponse} que contiene los resultados del análisis procesado.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Realizar análisis energético")
    @ApiResponse(responseCode = "201", description = "Análisis creado exitosamente")
    public AnalisisResponse crearAnalisis(@Valid @RequestBody AnalisisRequest request) {
        return service.realizarAnalisis(request);
    }

    /**
     * Consulta los detalles de un análisis energético existente a partir de su identificador único.
     *
     * @param id Identificador único del análisis a consultar, extraído de la ruta de la petición.
     * @return {@link ResponseEntity} que envuelve el {@link AnalisisResponse} con un estado HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar análisis por ID")
    @ApiResponse(responseCode = "200", description = "Análisis encontrado")
    public ResponseEntity<AnalisisResponse> consultarAnalisis(@PathVariable Long id) {
        return ResponseEntity.ok(service.consultarPorId(id));
    }
}