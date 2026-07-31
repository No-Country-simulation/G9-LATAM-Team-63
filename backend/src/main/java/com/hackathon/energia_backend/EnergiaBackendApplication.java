package com.hackathon.energia_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada principal de la aplicación Spring Boot.
 *
 * Esta clase actúa como el bootstrap de la aplicación, iniciando el contexto
 * de Spring y configurando automáticamente todos los componentes del sistema
 * mediante el escaneo de paquetes y la autoconfiguración basada en classpath.
 */
@SpringBootApplication
public class EnergiaBackendApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args Argumentos de línea de comandos pasados durante la ejecución.
     */
    // ============================================
    // Método Principal: Bootstrap de la Aplicación
    // Inicia el contenedor de Spring Boot y despliega
    // el contexto de la aplicación con:
    // - Autoconfiguración basada en dependencias
    // - Escaneo de componentes del paquete base
    // - Configuración del servidor embebido (Tomcat)
    // ============================================
    public static void main(String[] args) {
        SpringApplication.run(EnergiaBackendApplication.class, args);
    }

}