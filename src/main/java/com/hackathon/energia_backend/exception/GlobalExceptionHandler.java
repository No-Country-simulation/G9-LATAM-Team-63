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
 * Manejador global de excepciones.
 * @RestControllerAdvice intercepta las excepciones lanzadas en cualquier controlador
 * y retorna una respuesta JSON estandarizada en lugar de un error HTML genérico.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja los errores de validación (@Valid) cuando los datos de entrada son incorrectos.
     * Retorna un código 400 Bad Request con los detalles de qué campo falló.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Recorre todos los campos que fallaron la validación
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Datos de entrada inválidos");
        response.put("detalles", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones genéricas (como cuando no se encuentra un ID).
     * Retorna un código 404 Not Found o 500 Internal Server Error.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());

        // Si el mensaje contiene "no encontrado", retornamos 404, si no, 500
        HttpStatus status = ex.getMessage().contains("no encontrado") ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(response, status);
    }
}
