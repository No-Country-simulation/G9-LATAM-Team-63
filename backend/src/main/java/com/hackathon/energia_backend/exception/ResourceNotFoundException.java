package com.hackathon.energia_backend.exception;

/**
 * Excepción de dominio personalizada para indicar que un recurso solicitado no existe.
 *
 * El uso de excepciones específicas (en lugar de genéricas como {@code RuntimeException})
 * es una práctica recomendada en arquitectura enterprise porque:
 * <ul>
 *   <li><b>Manejo preciso:</b> Permite un mapeo directo y limpio en los {@code @ExceptionHandler}
 *       (ej. retornar automáticamente un HTTP 404 Not Found sin lógica condicional).</li>
 *   <li><b>Robustez:</b> Elimina la necesidad de evaluar frágilmente cadenas de texto
 *       (string matching) para determinar el tipo de error, como se hacía en el manejador genérico.</li>
 *   <li><b>Intención del código:</b> Hace que la capa de servicio sea más legible y expresiva
 *       al lanzar errores con nombres de dominio claros (ej. "Usuario no encontrado").</li>
 * </ul>
 */
public class ResourceNotFoundException extends RuntimeException {

    // ============================================
    // Constructor de la Excepción
    // Delega el mensaje descriptivo del error a la superclase RuntimeException.
    // Este mensaje será extraído posteriormente por el GlobalExceptionHandler
    // para construir la respuesta JSON de error estandarizada.
    // ============================================
    public ResourceNotFoundException(String message) {
        super(message);
    }
}