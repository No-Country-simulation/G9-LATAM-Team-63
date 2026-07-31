package com.hackathon.energia_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO (Data Transfer Object) que representa la estructura estandarizada
 * para todas las respuestas de error de la API.
 *
 * El uso de una estructura uniforme garantiza que los clientes de la API
 * (frontend, aplicaciones móviles, sistemas terceros) puedan manejar las
 * excepciones de manera predecible y consistente. Este diseño se alinea
 * con buenas prácticas de la industria, como RFC 7807 (Problem Details for HTTP APIs).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    // ============================================
    // Campo: Mensaje de Error General
    // Describe de forma concisa y de alto nivel qué salió mal.
    // Ejemplo: "Error de validación de datos", "Recurso no encontrado"
    // ============================================
    private String error;

    // ============================================
    // Campo: Detalles Específicos del Error
    // Mapa clave-valor utilizado para proporcionar contexto granular.
    // Es especialmente útil en errores de validación (ej. @Valid),
    // donde la clave es el nombre del campo (ej. "email") y el valor
    // es el mensaje de error específico de esa validación fallida.
    // ============================================
    private Map<String, String> detalles;
}