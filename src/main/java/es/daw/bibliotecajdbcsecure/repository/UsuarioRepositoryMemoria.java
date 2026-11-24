package es.daw.bibliotecajdbcsecure.repository;

import es.daw.bibliotecajdbcsecure.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Usuarios en memoria. En la UT04 se sustituirá por una implementación con JPA.
 * Contraseña de los tres: 1234 (guardada como hash BCrypt).
 */
@Repository
public class UsuarioRepositoryMemoria implements UsuarioRepository {

    private final List<Usuario> usuarios = List.of(
            Usuario.builder()
                    .id(1).username("admin").rol("ADMIN").activo(true)
                    .password("$2a$10$FE6Xa2yaXAVUf9v7DDQoF.bjxEGeJujmNH7o5T91IUG3qjc3z05Py")
                    .build(),
            Usuario.builder()
                    .id(2).username("ana").rol("USER").activo(true)
                    .password("$2a$10$07vZe7nhH1Oehrm51orcpeer3uYX1JPi5lLBWb.HW.JMX.llYoIaK")
                    .build(),
            Usuario.builder()
                    .id(3).username("luis").rol("USER").activo(false)
                    .password("$2a$10$EQIZl.C1bGRS.QitQ.2aPuM62n.iRSiiLBqTCIUwFC21c9U9FPdyK")
                    .build()
    );

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarios.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }
}
