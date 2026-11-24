package es.daw.bibliotecajdbcsecure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Públicas: portada, página de error y recursos estáticos
                        .requestMatchers("/", "/error", "/css/**", "/vendor/**").permitAll()
                        // Consola H2: solo para desarrollo
                        .requestMatchers("/h2-console/**").permitAll()
                        // Solo ADMIN: altas, ediciones y borrados
                        .requestMatchers("/libros/nuevo", "/libros/editar/**",
                                "/libros/guardar", "/libros/borrar/**").hasRole("ADMIN")
                        .requestMatchers("/autores/nuevo", "/autores/editar/**",
                                "/autores/guardar", "/autores/borrar/**").hasRole("ADMIN")
                        // ADMIN y USER: consultar y filtrar
                        .requestMatchers("/libros", "/autores").hasAnyRole("ADMIN", "USER")
                        // Cualquier otra cosa: al menos estar identificado
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
