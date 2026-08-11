package com.hackathon.energia_backend.config;


import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.enums.Rol;
import com.hackathon.energia_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;

//==============================================================
// * Inicializador de datos de arranque (Bootstrap).
//==============================================================
/**
 * <p>
 * Se ejecuta automáticamente al levantar la aplicación. Garantiza que exista
 * al menos un usuario administrador para poder gestionar el sistema sin necesidad
 * de insertar datos manualmente en la base de datos.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Evita duplicados: solo crea el admin si no existe
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = Usuario.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("hackathon2026"))
                    .roles(Set.of(Rol.ADMIN, Rol.USER))
                    .build();

            usuarioRepository.save(admin);
            System.out.println("=== Usuario 'admin' creado con roles: ADMIN, USER ===");
        }
    }
}