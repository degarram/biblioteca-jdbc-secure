package es.daw.bibliotecajdbcsecure.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Usuario de la biblioteca.
 * POJO sin dependencias de Spring Security: NO implementa UserDetails.
 * La traducción a UserDetails la hace UsuarioDetailsService (SRP).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Integer id;
    private String username;
    private String password;   // hash BCrypt
    private String rol;        // ADMIN o USER, sin prefijo ROLE_
    private boolean activo;
}