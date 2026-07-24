package com.hackathon.energia_backend.exception;

/**
 * Excepción personalizada para recursos no encontrados.
 * Permite un manejo más específico que RuntimeException genérico.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}