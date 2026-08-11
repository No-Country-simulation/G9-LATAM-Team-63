package com.hackathon.energia_backend.enums;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Deserializador personalizado para el enum Rol.
 * Convierte automáticamente el valor recibido a MAYÚSCULAS antes de mapear al enum.
 * Esto permite enviar "user", "User", "USER" indistintamente.
 */
public class RolDeserializer extends JsonDeserializer<Rol> {

    @Override
    public Rol deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String valor = parser.getValueAsString();

        if (valor == null || valor.isBlank()) {
            return null;
        }

        // FIX: Convertir a mayúsculas antes de buscar el enum
        return Rol.valueOf(valor.toUpperCase());
    }
}