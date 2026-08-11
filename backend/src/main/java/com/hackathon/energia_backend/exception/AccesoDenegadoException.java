package com.hackathon.energia_backend.exception;

/**
 * Excepción lanzada cuando un usuario autenticado intenta acceder
 * a un recurso que no le pertenece (por ejemplo, el análisis de otro usuario).
 *
 * El {@link GlobalExceptionHandler} la traduce a una respuesta HTTP 403 Forbidden.
 */
public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException(String message) {
        super(message);
    }
}
