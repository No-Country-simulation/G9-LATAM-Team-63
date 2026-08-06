package com.hackathon.energia_backend.service;

import com.hackathon.energia_backend.dto.request.RegistroRequest;
import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService{

    @Autowired
    private UsuarioRepository Repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(RegistroRequest request) {
        if (Repository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya se encuentra registrado");
        }

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        return Repository.save(usuario);
    }

    public Usuario loadUserByUsername(String username, String password) throws UsernameNotFoundException {
        Usuario usuario = Repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Verificación de la contraseña contra el hash BCrypt almacenado
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        return usuario;
    }
}
