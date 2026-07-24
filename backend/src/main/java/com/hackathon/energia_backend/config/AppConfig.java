package com.hackathon.energia_backend.config;


import org.springframework.context.annotation.Configuration;

/**
 * Configuraciones globales de la aplicación.
 * Aquí puedes centralizar constantes, beans personalizados, etc.
 */
@Configuration // tan pronto arranque es la parte donde se encuentran las reglas
public class AppConfig {

    /** Tarifa fija por kWh según el brief */
    public static final double TARIFA_KWH = 0.75;
}