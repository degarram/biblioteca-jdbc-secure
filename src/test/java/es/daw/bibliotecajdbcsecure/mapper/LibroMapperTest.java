package es.daw.bibliotecajdbcsecure.mapper;

import es.daw.bibliotecajdbcsecure.dto.LibroDto;
import es.daw.bibliotecajdbcsecure.model.Autor;
import es.daw.bibliotecajdbcsecure.model.Libro;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LibroMapperTest {

    private final LibroMapper mapper = new LibroMapper();

    @Test
    void toDtoCopiaIsbnYTitulo() {
        // Arrange
        Libro libro = new Libro();
        libro.setIsbn("9788490000076");
        libro.setTitulo("Rayuela");
        libro.setAutor(new Autor(7, "Julio Cortázar"));

        // Act
        LibroDto dto = mapper.toDto(libro);

        // Assert
        assertEquals("9788490000076", dto.getIsbn());
        assertEquals("Rayuela", dto.getTitulo());
    }
}