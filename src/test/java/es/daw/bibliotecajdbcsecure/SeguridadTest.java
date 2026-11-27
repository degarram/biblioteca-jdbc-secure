package es.daw.bibliotecajdbcsecure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SeguridadTest {

    @Autowired
    private MockMvc mockMvc;

    // ---------- Autorización (usuarios simulados con @WithMockUser) ----------

    @Test
    void anonimoEsRedirigidoAlLogin() throws Exception {
        mockMvc.perform(get("/libros"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void portadaEsPublica() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userPuedeVerLibros() throws Exception {
        mockMvc.perform(get("/libros"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNoPuedeAbrirFormularioDeAlta() throws Exception {
        mockMvc.perform(get("/libros/nuevo"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNoPuedeBorrarAunqueConozcaLaUrl() throws Exception {
        mockMvc.perform(post("/libros/borrar/9788490000076").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPuedeBorrar() throws Exception {
        // Ojo: borra de verdad el libro de H2. Usa un ISBN que ningún otro test necesita.
        mockMvc.perform(post("/libros/borrar/9788490001479").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/libros"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void sinTokenCsrfNiSiquieraAdminPuedeBorrar() throws Exception {
        mockMvc.perform(post("/libros/borrar/9788490000076"))
                .andExpect(status().isForbidden());
    }

    // ---------- Autenticación real (UsuarioDetailsService + BCrypt + repositorio) ----------

    @Test
    void loginCorrectoConUsuarioDelRepositorio() throws Exception {
        mockMvc.perform(formLogin("/login").user("admin").password("1234"))
                .andExpect(authenticated().withUsername("admin").withRoles("ADMIN"));
    }

    @Test
    void loginConContrasenaIncorrecta() throws Exception {
        mockMvc.perform(formLogin("/login").user("admin").password("mala"))
                .andExpect(unauthenticated());
    }

    @Test
    void usuarioDesactivadoNoPuedeEntrar() throws Exception {
        mockMvc.perform(formLogin("/login").user("luis").password("1234"))
                .andExpect(unauthenticated());
    }
}