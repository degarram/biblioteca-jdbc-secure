package es.daw.bibliotecajdbcsecure.service;

import es.daw.bibliotecajdbcsecure.model.Usuario;
import es.daw.bibliotecajdbcsecure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Adaptador entre nuestro modelo y Spring Security:
 * busca el Usuario y lo devuelve como UserDetails.
 */
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario " + username));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())      // el hash: Spring lo compara con matches()
                .roles(usuario.getRol())              // ADMIN → authority ROLE_ADMIN
                .disabled(!usuario.isActivo())
                .build();
    }
}
