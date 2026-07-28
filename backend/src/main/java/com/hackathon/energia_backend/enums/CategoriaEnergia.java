package com.hackathon.energia_backend.enums;

import lombok.Getter;

/**
 * Categorías de eficiencia energética con sus probabilidades base.
 */
@Getter
public enum CategoriaEnergia {
    INEFICIENTE("Ineficiente", 0.75),
    MODERADO("Moderado", 0.70),
    EFICIENTE("Eficiente", 0.85);

    private final String nombre;
    private final double probabilidadBase;

    // costructor
    CategoriaEnergia(String nombre, double probabilidadBase) {
        this.nombre = nombre;
        this.probabilidadBase = probabilidadBase;
    }
}