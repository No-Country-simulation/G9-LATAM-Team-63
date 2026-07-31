package com.hackathon.energia_backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * Configuración de zona horaria global para la aplicación.
 *
 * Esta clase establece la zona horaria por defecto que utilizará
 * toda la aplicación, asegurando consistencia en el manejo de fechas
 * y horas en todas las operaciones del sistema.
 */
@Configuration
public class TimeZoneConfig {

    // ============================================
    // Configuración de Zona Horaria
    // Se lee desde application.properties (app.timezone)
    // Valor por defecto: UTC
    // ============================================
    @Value("${app.timezone:UTC}")
    private String timeZoneId;

    // ============================================
    // Inicialización de Zona Horaria del Sistema
    // Se ejecuta automáticamente después de crear el bean
    // Establece la zona horaria global para toda la JVM
    // ============================================
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZoneId));

        System.out.println("=== Zona horaria del sistema: " + timeZoneId + " ===");
        System.out.println("=== Offset GMT: " + (TimeZone.getDefault().getRawOffset() / (1000 * 60 * 60)) + " ===");
    }
}