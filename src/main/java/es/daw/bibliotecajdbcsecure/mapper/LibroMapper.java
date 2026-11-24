package es.daw.bibliotecajdbcsecure.mapper;

import es.daw.bibliotecajdbcsecure.dto.LibroDto;
import es.daw.bibliotecajdbcsecure.model.Autor;
import es.daw.bibliotecajdbcsecure.model.Libro;
import org.springframework.stereotype.Component;

/** Convierte entre LibroDto y Libro. Solo convierte: no usa servicios ni repositorios. */
@Component
public class LibroMapper {

    /** Un libro que viene de la BD ya existe: si se abre su formulario, es una edición. */
    public LibroDto toDto(Libro libro) {
        LibroDto dto = new LibroDto();
        dto.setIsbn(libro.getIsbn());
        dto.setEdicion(true);
        dto.setTitulo(libro.getTitulo());
        dto.setAutorId(libro.getAutor().getId());
        dto.setAutorNombre(libro.getAutor().getNombre());
        dto.setFechaPublicacion(libro.getFechaPublicacion());
        return dto;
    }

    /**
     * El Libro resultante no está completo:
     * - id es null, porque el DTO no lo lleva.
     * - El autor solo lleva su id.
     * LibroService se encarga de completarlo.
     */
    public Libro toModelEntity(LibroDto dto) {
        Autor autor = new Autor(dto.getAutorId(), null);
        return new Libro(null, dto.getIsbn(), dto.getTitulo(), autor, dto.getFechaPublicacion());
    }
}
