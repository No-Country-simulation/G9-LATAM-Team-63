package com.hackathon.energia_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Componente utilitario para la gestión del ciclo de vida de tokens JWT (JSON Web Tokens).
 * <p>
 * Esta clase centraliza la lógica criptográfica necesaria para la autenticación stateless
 * de la API. garantizando la integridad, autenticidad y no repudio de los tokens.
 * <p>
 * <strong>Consideraciones de Resiliencia:</strong>
 * Se implementan valores por defecto robustos en la inyección de dependencias (@Value)
 * como mecanismo de contingencia (fallback) para evitar fallos de arranque (Fail-Fast)
 * en entornos donde el sistema de archivos puede tener retrasos de sincronización
 * (ej. carpetas de OneDrive o discos de red).
 * </p>
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta utilizada para la firma y verificación criptográfica de los tokens.
     * <p>
     * Se inyecta desde el archivo de configuración {@code application.properties}.
     * El valor por defecto (después de los dos puntos ':') asegura que la aplicación
     * pueda arrancar incluso si el archivo de propiedades no es leído inmediatamente
     * por el classloader, cumpliendo con el requisito mínimo de 256 bits (32 caracteres)
     * para el algoritmo HS256.
     * </p>
     */
    @Value("${app.jwt.secret:MiClaveSecretaSuperSeguraDeAlMenos32CaracteresParaJWT!!!}")
    private String secretKey;

    /**
     * Tiempo de vida (TTL - Time To Live) del token en milisegundos.
     * <p>
     * Define la ventana de validez del token antes de que sea considerado expirado.
     * El valor por defecto de 86,400,000 ms equivale a 24 horas, un estándar balanceado
     * entre la usabilidad del usuario (no tener que loguearse constantemente) y la
     * reducción de la superficie de ataque en caso de compromiso del token.
     * </p>
     */
    @Value("${app.jwt.expiration-ms:86400000}")
    private long expirationMs;

    /**
     * Genera la clave criptográfica {@link SecretKey} a partir de la cadena de texto configurada.
     * <p>
     * Utiliza {@link Keys#hmacShaKeyFor(byte[])} de la librería JJWT, que es el método
     * recomendado por la especificación JCA (Java Cryptography Architecture) para derivar
     * claves HMAC seguras, evitando vulnerabilidades asociadas a la codificación manual de bytes.
     * </p>
     *
     * @return Instancia de {@link SecretKey} lista para operaciones de firma y verificación.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT firmado para un usuario autenticado.
     * <p>
     * Construye el token estableciendo los claims registrados (Registered Claims) esenciales:
     * <ul>
     *   <li><strong>sub (Subject):</strong> Identificador único del usuario (username).</li>
     *   <li><strong>id:</strong> Identificador numérico del usuario en la base de datos.</li>
     *   <li><strong>iat (Issued At):</strong> Marca de tiempo de emisión del token.</li>
     *   <li><strong>exp (Expiration):</strong> Marca de tiempo absoluta de expiración.</li>
     * </ul>
     * </p>
     *
     * @param username Identificador del sujeto para el cual se emite el token.
     * @param userId   Identificador numérico del usuario en la base de datos.
     * @return Cadena de texto compacta, codificada en Base64URL, que representa el JWT firmado.
     */
    public String generateToken(String username, Long userId) {
        return Jwts.builder()
                .subject(username)
                .claim("id", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el identificador del sujeto (username) desde un token JWT válido.
     *
     * @param token Cadena de texto del JWT a procesar.
     * @return El valor del claim {@code sub} contenido en el payload del token.
     * @throws JwtException si el token está mal formado, ha expirado o la firma es inválida.
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Valida la integridad criptográfica y la vigencia temporal de un token JWT.
     * <p>
     * Este método actúa como un guardián (guard clause) que verifica:
     * <ol>
     *   <li>Que la firma HMAC-SHA256 coincida con la clave secreta del servidor (integridad).</li>
     *   <li>Que la marca de tiempo actual sea anterior a la claim {@code exp} (vigencia).</li>
     *   <li>Que la estructura del token sea sintácticamente correcta.</li>
     * </ol>
     * Cualquier anomalía resulta en un rechazo silencioso (retorno de {@code false}) para
     * evitar fugas de información (Information Disclosure) sobre la naturaleza del fallo al cliente.
     * </p>
     *
     * @param token Cadena de texto del JWT a validar.
     * @return {@code true} si el token es auténtico y está dentro de su ventana de validez; {@code false} en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Fallo controlado: El token es inválido, ha expirado, o la firma no coincide.
            // Se retorna false para mantener la statelessness y no interrumpir el flujo del filtro.
            return false;
        }
    }

    /**
     * Parsea y valida el token JWT, extrayendo su carga útil (payload) o claims.
     * <p>
     * Este es el núcleo de la validación. El parser de JJWT verifica automáticamente la firma
     * utilizando la clave proporcionada y lanza excepciones específicas ({@link io.jsonwebtoken.ExpiredJwtException},
     * {@link io.jsonwebtoken.SignatureException}, etc.) si alguna condición de seguridad no se cumple.
     * </p>
     *
     * @param token Cadena de texto del JWT a parsear.
     * @return Objeto {@link Claims} que contiene todos los datos del payload verificado.
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}