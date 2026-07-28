package com.hackathon.energia_backend.enums;

import lombok.Getter;

/**
 * Enumeración de tipos de inmueble válidos.
 * Reemplaza el @Pattern con validación tipada.
 */
@Getter
public enum TipoInmueble {
    CASA("Casa"),
    DEPTO("Apartamento"),
    LOCAL("Local"),
    OFICINA("Oficina");


    private final String valor;

    TipoInmueble(String valor) {
        this.valor = valor;
    }

    public static TipoInmueble fromString(String text) {
        for (TipoInmueble tipo : TipoInmueble.values()) {
            if (tipo.valor.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de inmueble no válido: " + text);
    }
}