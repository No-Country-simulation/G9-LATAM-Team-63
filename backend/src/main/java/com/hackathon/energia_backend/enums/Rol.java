package com.hackathon.energia_backend.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Enumeración de roles de seguridad del dominio.
 * <p>
 * Cada rol representa un nivel de privilegio dentro de la aplicación.
 * Spring Security los interpreta con el prefijo <code>ROLE_</code> automáticamente
 * al usarlos con {@code hasRole()} o {@code hasAnyRole()}.
 * </p>
 */
@JsonDeserialize (using = RolDeserializer.class)
public enum Rol {
    /** Rol con privilegios completos: lectura, escritura, modificación y eliminación */
    ADMIN,

    /** Rol estándar: lectura y escritura básica */
    USER,

}
