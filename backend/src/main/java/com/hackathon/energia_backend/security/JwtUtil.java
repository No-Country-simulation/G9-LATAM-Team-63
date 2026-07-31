package com.hackathon.energia_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Componente utilitario para la generación, validación y extracción de tokens JWT.
 *
 * Esta clase centraliza toda la lógica relacionada con JSON Web Tokens (JWT),
 * proporcionando métodos seguros para la autenticación stateless de la API.
 * Utiliza el algoritmo HMAC-SHA256 para la firma de tokens y valida automáticamente
 * la expiración e integridad de los mismos.
 */
@Component
public class JwtUtil {

    // ============================================
    // Configuración de Clave Secreta JWT
    // Se lee desde application.properties (app.jwt.secret)
    // Esta clave se utiliza para firmar y verificar los tokens
    // ============================================
    @Value("${app.jwt.secret}")
    private String secretKey;

    // ============================================
    // Configuración de Tiempo de Expiración
    // Se lee desde application.properties (app.jwt.expiration-ms)
    // Define la duración del token en milisegundos
    // ============================================
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    // ============================================
    // Generación de Clave Criptográfica
    // Convierte la clave secreta de texto en una clave HMAC-SHA256
    // segura para la firma de tokens JWT
    // ============================================
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * @param username Nombre de usuario para el cual se genera el token.
     * @return Token JWT firmado y listo para ser enviado al cliente.
     */
    // ============================================
    // Método: Generar Token JWT
    // Crea un token con:
    // - Subject: username del usuario
    // - IssuedAt: fecha/hora actual de emisión
    // - Expiration: fecha/hora de vencimiento calculada
    // - Signature: firma HMAC-SHA256 con la clave secreta
    // ============================================
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el nombre de usuario almacenado en el token JWT.
     *
     * @param token Token JWT válido del cual extraer la información.
     * @return Nombre de usuario contenido en el claim "sub" (subject).
     */
    // ============================================
    // Método: Extraer Username del Token
    // Accede al payload del token y recupera el claim "subject"
    // que contiene el nombre de usuario autenticado
    // ============================================
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Valida la integridad y vigencia de un token JWT.
     *
     * @param token Token JWT a validar.
     * @return true si el token es válido y no ha expirado, false en caso contrario.
     */
    // ============================================
    // Método: Validar Token JWT
    // Verifica:
    // - Firma criptográfica válida (no ha sido alterado)
    // - Token no ha expirado (expiration > now)
    // - Formato JWT correcto
    // Retorna false si hay cualquier excepción de validación
    // ============================================
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token inválido, expirado o mal firmado
            return false;
        }
    }

    // ============================================
    // Método Privado: Obtener Claims del Token
    // Parsea el token JWT, verifica la firma con la clave secreta
    // y retorna el payload completo (claims) para su procesamiento
    // ============================================
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}