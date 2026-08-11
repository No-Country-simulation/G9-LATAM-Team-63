package com.hackathon.energia_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//=============================================================================================
// * Componente utilitario para la gestión del ciclo de vida de tokens JWT.
//=============================================================================================
/**
 * <p>
 * <strong>Cambio realizado:</strong> El token ahora incluye un claim <code>roles</code>
 * que transporta las authorities del usuario autenticado. Esto permite que el
 * {@link JwtAuthenticationFilter} reconstruya el contexto de seguridad con los permisos
 * correctos sin necesidad de consultar la base de datos en cada request.
 * </p>
 */
@Component
public class JwtUtil {

    @Value("${app.jwt.secret:MiClaveSecretaSuperSeguraDeAlMenos32CaracteresParaJWT!!!}")
    private String secretKey;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //=================================================================================================
    // * Genera un token JWT firmado incluyendo el subject (username) y los roles/authorities.
    //=================================================================================================
    /**
<<<<<<< HEAD
     * @param userDetails Detalles del usuario autenticado.
     * @return Token JWT compacto y firmado.
     */
    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
=======
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
>>>>>>> 1bf5e3c056f6c589018e6740dfc590644ddeec8f
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    //===================================================================================
    //* Extrae la lista de roles/authorities inyectados en el payload del token.
    // ===================================================================================
    /**
     *
     * @param token JWT válido.
     * @return Lista de strings con formato "ROLE_XXX".
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}