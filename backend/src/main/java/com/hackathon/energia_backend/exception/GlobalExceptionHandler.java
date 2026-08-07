package com.hackathon.energia_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Componente centralizado para el manejo de excepciones a nivel global en la aplicación.
 *
 * La anotación {@code @RestControllerAdvice} intercepta las excepciones no controladas
 * lanzadas por cualquier {@code @RestController}, garantizando que la API siempre responda
 * con un payload JSON estandarizado (coherente con la estructura de ErrorResponse)
 * en lugar de la página de error HTML por defecto de Spring Boot.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============================================
    // Manejador: Errores de Validación de Datos
    // Intercepta fallos en anotaciones @Valid o @Validated
    // Respuesta HTTP: 400 Bad Request
    // ============================================

    /**
     * Procesa las excepciones lanzadas cuando los datos de entrada no cumplen
     * con las reglas de validación definidas en los DTOs.
     *
     * @param ex La excepción {@link MethodArgumentNotValidException} lanzada por Spring.
     * @return {@link ResponseEntity} con un mapa que contiene el error general
     *         y un desglose de los campos específicos que fallaron.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Mapa para almacenar los errores específicos de cada campo (clave: nombre del campo, valor: mensaje)
        Map<String, String> fieldErrors = new HashMap<>();

        // Itera sobre todos los errores de validación registrados en el BindingResult
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        // Construcción del payload de respuesta estandarizado
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Datos de entrada inválidos");
        response.put("detalles", fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ============================================
    // Manejador: Acceso Denegado (Recurso de Otro Usuario)
    // Intercepta intentos de acceder a un análisis que no pertenece
    // al usuario autenticado.
    // Respuesta HTTP: 403 Forbidden
    // ============================================

    /**
     * Procesa la excepción {@link AccesoDenegadoException} lanzada cuando
     * un usuario intenta consultar un recurso que no le pertenece.
     *
     * @param ex La excepción {@link AccesoDenegadoException} lanzada por la aplicación.
     * @return {@link ResponseEntity} con el mensaje de error y estado HTTP 403.
     */
    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Map<String, String>> handleAccesoDenegado(AccesoDenegadoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // ============================================
    // Manejador: Excepciones de Tiempo de Ejecución (Genéricas)
    // Captura RuntimeExceptions no manejadas por otros handlers específicos
    // Respuesta HTTP: 404 Not Found o 500 Internal Server Error
    // ============================================

    /**
     * Procesa excepciones de tiempo de ejecución no controladas.
     * Evalúa el mensaje de la excepción para determinar el código de estado
     * HTTP más adecuado de forma dinámica.
     *
     * @param ex La excepción {@link RuntimeException} lanzada por la aplicación.
     * @return {@link ResponseEntity} con el mensaje de error y el estado HTTP correspondiente.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());

        // Evaluación heurística del mensaje para asignar el código de estado
        // Nota: En una arquitectura más madura, se recomendaría usar excepciones
        // personalizadas (ej. ResourceNotFoundException) en lugar de evaluar strings.
        HttpStatus status = ex.getMessage().toLowerCase().contains("no encontrado")
                ? HttpStatus.NOT_FOUND
                : HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(response, status);
    }
}