package com.hackathon.energia_backend.config;

import com.hackathon.energia_backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * ================================================================================================
 * Configuración central de seguridad para la aplicación Spring Boot.
 *
 * Fix aplicado (Simplificación de roles):
 *   - Se removió MODERATOR del enum de dominio y de todas las expresiones hasAnyRole().
 *   - La operación GET sobre /api/usuarios/** ahora solo admite ADMIN y USER.
 * ================================================================================================
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // =========================================================================
    // 1. Lista centralizada de rutas públicas (evita duplicar en el filtro JWT)
    // =========================================================================
    private static final String[] PUBLIC_PATHS = {
            "/swagger-custom.html",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/swagger-resources/**",
            "/webjars/**",
            "/h2-console/**",
            "/error"                    // Evita 403 en páginas de error de Spring Boot
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("=======================================================================");
        log.info(" [SECURITY] Iniciando configuración de SecurityFilterChain...");
        log.info(" [SECURITY] Rutas públicas registradas: {}", Arrays.toString(PUBLIC_PATHS));
        log.info(" [SECURITY] Modelo de roles activo: ADMIN | USER");
        log.info("=======================================================================");

        http
                // -----------------------------------------------------------------
                // Desactivar CSRF (API stateless)
                // -----------------------------------------------------------------
                .csrf(csrf -> csrf.disable())

                // -----------------------------------------------------------------
                // Configuración CORS
                // -----------------------------------------------------------------
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // -----------------------------------------------------------------
                // Reglas de autorización por URL (orden: más específico → más genérico)
                // -----------------------------------------------------------------
                .authorizeHttpRequests(auth -> auth
                        // 1. Documentación y recursos estáticos (sin autenticación)
                        .requestMatchers(PUBLIC_PATHS).permitAll()

                        // 2. Endpoints de autenticación (login/register)
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        // 3. Gestión de usuarios con roles (solo ADMIN y USER)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")

                        // 4. Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )

                // -----------------------------------------------------------------
                // Sin sesiones (JWT stateless)
                // -----------------------------------------------------------------
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // -----------------------------------------------------------------
                // Filtro JWT antes del filtro de autenticación de usuario/contraseña
                // NOTA: El filtro JWT DEBE tener lógica para saltar rutas públicas
                // -----------------------------------------------------------------
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // -----------------------------------------------------------------
                // Headers para H2 Console (frames)
                // -----------------------------------------------------------------
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        log.info("[SECURITY] SecurityFilterChain configurado exitosamente.");
        return http.build();
    }

    /**
     * Configuración CORS para permitir peticiones desde el frontend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",
                "http://localhost:3000",
                "http://localhost:4200"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin"
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}