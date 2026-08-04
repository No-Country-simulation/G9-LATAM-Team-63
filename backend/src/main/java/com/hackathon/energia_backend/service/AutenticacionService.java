package com.hackathon.energia_backend.service;

import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService{

    @Autowired
    private UsuarioRepository Repository;

    public Usuario loadUserByUsername(String username, String password) throws UsernameNotFoundException {
        Usuario usuario = Repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Comparación en texto plano
        if (!password.equals(usuario.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        return usuario;
    }
}
