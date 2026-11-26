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
                // AUTORIZACIÓN: qué rutas son públicas
                .authorizeHttpRequests(auth -> auth
                        // Públicas: portada, página de error y recursos estáticos
                        .requestMatchers("/", "/error", "/css/**", "/vendor/**").permitAll()
                        // Consola H2: solo para desarrollo
                        .requestMatchers("/h2-console/**").permitAll()
                        // Solo ADMIN: borrados
                        .requestMatchers("/libros/borrar/**", "/autores/borrar/**").hasRole("ADMIN")
                        // ADMIN y BIBLIOTECARIO: crear y editar
                        .requestMatchers("/libros/nuevo", "/libros/editar/**",
                                "/libros/guardar","/autores/nuevo", "/autores/editar/**",
                                "/autores/guardar").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                        // ADMIN y BIBLIOTECARIO y USER: consultar y filtrar
                        .requestMatchers("/libros", "/autores").hasAnyRole("ADMIN", "BIBLIOTECARIO", "USER")
                        // Cualquier otra cosa: al menos estar identificado
                        .anyRequest().authenticated()
                )
                // AUTENTICACIÓN: formulario de login propio
                .formLogin(form -> form
                        .loginPage("/login")              // GET /login: nuestra vista
                        .defaultSuccessUrl("/libros")     // a dónde ir tras entrar
                        .permitAll()
                )
                // LOGOUT: POST /logout (con token CSRF)
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // La consola H2 no envía token CSRF y se muestra dentro de un <iframe>
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
