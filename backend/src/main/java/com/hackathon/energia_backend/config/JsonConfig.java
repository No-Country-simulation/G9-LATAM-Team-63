package com.hackathon.energia_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import java.util.TimeZone;

@Configuration
public class JsonConfig {

    // ============================================
    // Configuración de zona horaria
    // Permite personalizar la zona horaria desde application.properties
    // ============================================
    @Value("${app.timezone:UTC}")
    private String timeZoneId;

    // ============================================
    // Configuración de ObjectMapper para Jackson
    // Personaliza la serialización/deserialización JSON
    // ============================================
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Establece la zona horaria para el manejo de fechas
        mapper.setTimeZone(TimeZone.getTimeZone(timeZoneId));

        // Registra el módulo para soportar Java 8 Time API (LocalDate, LocalDateTime, etc.)
        mapper.registerModule(new JavaTimeModule());

        // Desactiva la escritura de fechas como timestamps numéricos
        // Esto hace que las fechas se serialicen como string
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        System.out.println("=== Jackson configurado con zona horaria: " + timeZoneId + " ===");

        return mapper;
    }
}