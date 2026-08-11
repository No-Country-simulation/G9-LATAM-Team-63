package com.hackathon.energia_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ================================================================================================
 * Filtro de autenticación JWT que intercepta todas las peticiones HTTP.
 *
 * <p><strong>Fix aplicado (Swagger 403):</strong></p>
 * <ul>
 *   <li>Se sobrescribió el método {@link #shouldNotFilter(HttpServletRequest)} para excluir
 *       rutas públicas (Swagger, autenticación, recursos estáticos, H2 Console).</li>
 *   <li>Esto evita que el filtro JWT procese peticiones a <code>/v3/api-docs</code> o
 *       <code>/swagger-ui/**</code>, permitiendo que <code>permitAll()</code> de
 *       {@code SecurityConfig} actúe correctamente.</li>
 *   <li>Antes, el filtro se ejecutaba en TODAS las peticiones y el contexto de seguridad
 *       quedaba vacío, provocando que Spring Security devolviera 403 en rutas públicas.</li>
 * </ul>
 * ================================================================================================
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // =========================================================================
    // FIX: Lista de rutas públicas que el filtro JWT debe ignorar completamente.
    // Se comparan por prefijo (startsWith) para soportar wildcards sin necesidad
    // de clases externas como AntPathRequestMatcher.
    // =========================================================================
    private static final List<String> PUBLIC_PATHS = List.of(
            "/swagger-custom.html",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/v3/api-docs/",
            "/v3/api-docs",
            "/swagger-resources/",
            "/webjars/",
            "/h2-console/",
            "/api/auth/",
            "/error"
    );

    /**
     * Determina si este filtro NO debe ejecutarse para la petición actual.
     * Si la URI coincide con alguna ruta pública, Spring omite completamente
     * {@link #doFilterInternal} y deja pasar la petición sin validar JWT.
     *
     * @param request la petición HTTP entrante
     * @return {@code true} si la petición está exenta de autenticación JWT
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        // Si la URI comienza con alguna de las rutas públicas, el filtro NO actúa.
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(uri::startsWith);

        if (isPublic) {
            logger.debug("[JWT FILTER] Saltando validación JWT para ruta pública: " + uri);
        }

        return isPublic;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // ---------------------------------------------------------------------
        // Si no hay header o no empieza con "Bearer ", no hacemos nada
        // y dejamos que la cadena de filtros continúe.
        // ---------------------------------------------------------------------
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // ---------------------------------------------------------------------
        // Validar token y establecer autenticación en el contexto de seguridad
        // ---------------------------------------------------------------------
        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);

            // ============================================
            // Extraer roles del token y construir authorities dinámicas
            // ============================================
            List<String> roles = jwtUtil.extractRoles(token);
            var authorities = roles.stream()
                    .map(rol -> rol.startsWith("ROLE_") ? rol : "ROLE_" + rol)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

<<<<<<< HEAD
=======
            // ============================================
            // Paso 6: Establecer Contexto de Autenticación
            // Crea el token de autenticación usando la ENTIDAD Usuario como
            // principal (no solo el username), para que los controladores
            // puedan inyectarla con @AuthenticationPrincipal y acceder
            // directamente al id del usuario sin consultas adicionales.
            // Lo registra en SecurityContextHolder para que esté disponible
            // en toda la ejecución del request
            // ============================================
>>>>>>> 1bf5e3c056f6c589018e6740dfc590644ddeec8f
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}