package es.daw.bibliotecajdbcsecure.repository;

import es.daw.bibliotecajdbcsecure.model.Usuario;

import java.util.Optional;

/**
 * Solo lo que necesita la autenticación.
 * No extiende GenericRepository: nadie da de alta ni borra usuarios desde la aplicación (ISP).
 */
public interface UsuarioRepository {

    Optional<Usuario> findByUsername(String username);
}
