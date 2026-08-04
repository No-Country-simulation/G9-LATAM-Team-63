package com.hackathon.energia_backend.security;

import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Filtro de autenticación JWT que intercepta todas las peticiones HTTP.
 *
 * Este componente valida los tokens JWT en cada request, extrae la información
 * del usuario y establece el contexto de seguridad en Spring Security.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // ============================================
    // Inyección de Dependencias
    // Utilidad para validación y extracción de datos del token JWT
    // ============================================
    private final JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // ============================================
        // Paso 1: Obtener Header de Autorización
        // Extrae el header "Authorization" de la petición HTTP
        // ============================================
        String authHeader = request.getHeader("Authorization");

        // ============================================
        // Paso 2: Validar Formato del Token
        // Verifica que el header exista y comience con "Bearer "
        // Si no es válido, continúa la cadena de filtros sin autenticar
        // ============================================
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ============================================
        // Paso 3: Extraer Token JWT
        // Remueve el prefijo "Bearer " (7 caracteres) para obtener
        // únicamente el token JWT puro
        // ============================================
        String token = authHeader.substring(7);

        // ============================================
        // Paso 4: Validar Token y Extraer Información
        // Verifica la validez del token y extrae el username
        // ============================================
        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            Usuario usuario = repository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // ============================================
            // Paso 5: Crear Autoridad de Seguridad
            // Asigna el rol ROLE_USER al usuario autenticado
            // CRÍTICO: Spring Security requiere al menos una autoridad
            // para considerar al usuario como autenticado
            // ============================================
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

            // ============================================
            // Paso 6: Establecer Contexto de Autenticación
            // Crea el token de autenticación con username y autoridades
            // Lo registra en SecurityContextHolder para que esté disponible
            // en toda la ejecución del request
            // ============================================
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(authority)
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // ============================================
        // Paso 7: Continuar Cadena de Filtros
        // Permite que el request continúe hacia el siguiente filtro
        // o el controlador destino con el contexto de seguridad establecido
        // ============================================
        filterChain.doFilter(request, response);
    }
}