package com.hackathon.energia_backend.config;

import com.hackathon.energia_backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

/**
 * Configuración central de seguridad para la aplicación Spring Boot.
 * <p>
 * Esta clase es responsable de definir la cadena de filtros de seguridad (SecurityFilterChain),
 * establecer las reglas de autorización por rutas, configurar la gestión de sesiones como
 * STATELESS (sin estado) para APIs RESTful, e integrar el filtro personalizado de validación JWT.
 * </p>
 *
 * @author Backend Specialist
 * @version 1.1
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;



    /**
     * Configura y personaliza la cadena de filtros de seguridad HTTP.
     *
     * @param http El objeto {@link HttpSecurity} proporcionado por Spring Security.
     * @return Una instancia configurada de {@link SecurityFilterChain}.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TRAZABILIDAD: Este log confirmará que Spring está cargando ESTA versión del archivo
        log.info("=======================================================================");
        log.info("⚙️ [SECURITY] Configurando SecurityFilterChain con rutas públicas de Swagger...");
        log.info("=======================================================================");

        http
                // =====================================================================
                // 1. Desactivación de CSRF (Cross-Site Request Forgery)
                // En una API REST stateless que utiliza tokens JWT en los headers
                // en lugar de cookies de sesión, la protección CSRF no es necesaria.
                // =====================================================================
                .csrf(csrf -> csrf.disable())

                // =====================================================================
                // 2. Configuración de CORS (Cross-Origin Resource Sharing)
                // Permite que clientes frontend o herramientas como Swagger/Postman
                // realicen peticiones desde diferentes orígenes de manera controlada.
                // =====================================================================
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // =====================================================================
                // 3. Reglas de Autorización de Rutas (Endpoint Security)
                // =====================================================================
                .authorizeHttpRequests(auth -> auth
                        // 3.1. Rutas PÚBLICAS: No requieren token JWT
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()// Permitir acceso público a /login
                        .requestMatchers(
                                "/swagger-custom.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/api/auth/**",
                                "/h2-console/**"
                        ).permitAll()

                        // 3.2. Rutas PROTEGIDAS: Requieren autenticación JWT válida
                        .anyRequest().authenticated()
                )

                // =====================================================================
                // 4. Gestión de Sesiones STATELESS (Sin estado)
                // Spring Security no creará ni utilizará sesiones HTTP (HttpSession).
                // =====================================================================
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // =====================================================================
                // 5. Integración del Filtro JWT Personalizado
                // Se ejecuta ANTES del filtro de autenticación por defecto de Spring.
                // =====================================================================
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // =====================================================================
                // 6. Configuración de Cabeceras de Seguridad (Headers)
                // Permite que la consola H2 se renderice dentro de un iframe.
                // =====================================================================
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        log.info("✅ [SECURITY] SecurityFilterChain configurado exitosamente.");
        return http.build();
    }

    /**
     * Configura las políticas de CORS para la aplicación.
     *
     * @return Una instancia de {@link CorsConfigurationSource} con las reglas definidas.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos (Desarrollo local)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",
                "http://localhost:3000",
                "http://localhost:4200",
                "http://localhost:5173"
        ));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Cabeceras permitidas (incluyendo 'Authorization' para el token JWT)
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin"
        ));

        // Permitir el envío de credenciales
        configuration.setAllowCredentials(true);

        // Aplicar esta configuración a todas las rutas
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