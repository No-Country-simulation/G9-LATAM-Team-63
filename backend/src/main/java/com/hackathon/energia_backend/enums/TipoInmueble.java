package com.hackathon.energia_backend.enums;

import lombok.Getter;

/**
 * Enumeración que define los tipos de inmueble válidos en el dominio de la aplicación.
 *
 * El uso de esta enumeración reemplaza la validación por expresiones regulares (@Pattern),
 * ofreciendo las siguientes ventajas arquitectónicas:
 * <ul>
 *   <li><b>Seguridad de tipos (Type Safety):</b> Previene errores de escritura en tiempo de compilación.</li>
 *   <li><b>Principio DRY (Don't Repeat Yourself):</b> Centraliza los valores permitidos en un único punto de verdad.</li>
 *   <li><b>Refactorización segura:</b> Facilita cambios futuros sin tener que buscar cadenas de texto dispersas en el código.</li>
 * </ul>
 */
@Getter
public enum TipoInmueble {

    // ============================================
    // Constantes de la Enumeración
    // Representan los estados válidos del dominio para el tipo de inmueble.
    // El identificador (ej. DEPTO) puede diferir del valor de negocio (ej. "Apartamento").
    // ============================================
    CASA("Casa"),
    DEPTO("Apartamento"),
    LOCAL("Local"),
    OFICINA("Oficina");

    // ============================================
    // Atributo de Estado (Inmutable)
    // Almacena la representación en cadena de texto para serialización JSON o UI.
    // Se declara como 'final' para garantizar la inmutabilidad de la constante.
    // ============================================
    private final String valor;

    // ============================================
    // Constructor de la Enumeración
    // Visibilidad package-private (por defecto), que es la práctica estándar
    // y recomendada en Java para la inicialización de constantes de enumeración.
    // ============================================
    TipoInmueble(String valor) {
        this.valor = valor;
    }

    /**
     * Método de fábrica para convertir una cadena de texto en su equivalente Enum.
     *
     * @param text Cadena de texto a convertir (ej. "casa", "CASA", "Casa").
     * @return La instancia de {@link TipoInmueble} correspondiente.
     * @throws IllegalArgumentException si el texto no coincide con ningún tipo válido.
     */
    // ============================================
    // Método de Fábrica (Factory Method)
    // Permite la conversión segura de String a Enum con validación case-insensitive.
    // Aplica el principio "Fail-Fast" lanzando una excepción ante datos inválidos.
    // ============================================
    public static TipoInmueble fromString(String text) {
        // Búsqueda lineal insensible a mayúsculas/minúsculas
        for (TipoInmueble tipo : TipoInmueble.values()) {
            if (tipo.valor.equalsIgnoreCase(text)) {
                return tipo;
            }
        }

        // Fallo rápido (Fail-Fast) para evitar estados inconsistentes en el dominio
        throw new IllegalArgumentException("Tipo de inmueble no válido: " + text);
    }
}