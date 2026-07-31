package com.hackathon.energia_backend.enums;

import lombok.Getter;

/**
 * Enumeración que define las categorías de eficiencia energética del dominio.
 *
 * El uso de un Enum en lugar de cadenas de texto (Strings) garantiza:
 * <ul>
 *   <li><b>Seguridad de tipos (Type Safety):</b> Evita errores por valores escritos incorrectamente.</li>
 *   <li><b>Centralización de reglas de negocio:</b> Asocia cada categoría con su probabilidad base de forma inmutable.</li>
 *   <li><b>Legibilidad:</b> Elimina los "magic strings" o "magic numbers" del código de la aplicación.</li>
 * </ul>
  */
@Getter
public enum CategoriaEnergia {

    // ============================================
    // Constantes de la Enumeración
    // Cada instancia representa un estado válido del dominio
    // ============================================

    /** Consumo ineficiente con probabilidad base del 75% */
    INEFICIENTE("Ineficiente", 0.75),

    /** Consumo moderado con probabilidad base del 70% */
    MODERADO("Moderado", 0.70),

    /** Consumo eficiente con probabilidad base del 85% */
    EFICIENTE("Eficiente", 0.85);

    // ============================================
    // Atributos de Estado (Inmutables)
    // Se declaran como 'final' para garantizar que no puedan
    // ser modificados después de la inicialización del Enum.
    // ============================================

    /** Nombre legible de la categoría para la presentación o serialización */
    private final String nombre;

    /** Probabilidad base asociada a esta categoría para cálculos del motor de reglas */
    private final double probabilidadBase;

    // ============================================
    // Constructor de la Enumeración
    // Visibilidad por defecto (package-private), lo cual es el estándar
    // y la práctica recomendada en Java para constructores de Enums.
    // ============================================
    CategoriaEnergia(String nombre, double probabilidadBase) {
        this.nombre = nombre;
        this.probabilidadBase = probabilidadBase;
    }
}