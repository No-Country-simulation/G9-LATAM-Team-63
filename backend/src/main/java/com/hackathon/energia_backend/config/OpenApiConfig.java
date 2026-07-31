package com.hackathon.energia_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // ============================================
                // Información de la API
                // ============================================
                .info(new Info()
                        .title("Energia Backend API")
                        .version("1.0.0")
                        .description("API REST para gestión de energía")
                        .contact(new Contact()
                                .name("Tu Nombre")
                                .email("tu@email.com")))

                // ============================================
                //  Configuración de Seguridad JWT
                // Esto agrega un botón "Authorize" en Swagger
                // ============================================
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))

                // ============================================
                // Esquema de autenticación Bearer
                // Define cómo se debe proporcionar el token
                // ============================================
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa el token JWT obtenido del endpoint /api/auth/login. " +
                                                "Formato: Bearer <tu-token>")));
    }
}