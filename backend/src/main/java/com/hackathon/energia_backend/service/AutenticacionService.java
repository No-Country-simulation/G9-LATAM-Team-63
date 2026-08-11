package com.hackathon.energia_backend.service;

import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

//==================================================================================
// * Servicio de autenticación que implementa {@link UserDetailsService}.
//==================================================================================
/**
 * <p>
 * <strong>Cambio realizado:</strong> Ahora implementa la interfaz estándar de Spring Security
 * para convertir la entidad {@link Usuario} en un {@link UserDetails} con sus roles/authorities.
 * Además, utiliza {@link PasswordEncoder} para validar credenciales de forma segura.
 * </p>
 */

@Service
public class AutenticacionService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //==========================================================================================
    //* Carga un usuario por su username y lo adapta al contrato {@link UserDetails}
    //==========================================================================================
    /**
     * que Spring Security utiliza para la autorización.
     * @param username Identificador del usuario.
     * @return UserDetails con username, password hasheada y authorities.
     * @throws UsernameNotFoundException si el usuario no existe.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.name()))
                        .collect(Collectors.toList())
        );
    }

    //=========================================================================================
    // * Valida credenciales comparando la contraseña en texto plano con la versión hasheada
    //=========================================================================================
    /**
     * almacenada en la base de datos.
     * @param username    Username ingresado.
     * @param rawPassword Contraseña en texto plano.
     * @return UserDetails autenticado si las credenciales son válidas.
     * @throws BadCredentialsException si la contraseña no coincide.
     */
    public UserDetails authenticate(String username, String rawPassword) {
        UserDetails user = loadUserByUsername(username);
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
        return user;
    }
}
