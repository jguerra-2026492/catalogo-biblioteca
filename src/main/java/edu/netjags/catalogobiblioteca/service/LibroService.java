package edu.netjags.catalogobiblioteca.digital.service;

import edu.netjags.catalogobiblioteca.digital.model.Libro;
import edu.netjags.catalogobiblioteca.digital.repository.LibroRepository;
import edu.netjags.catalogobiblioteca.digital.repository.LibroRepositoryImpl;

import java.sql.SQLException;
import java.time.Year;
import java.util.List;

/**
 * Capa de logica de negocio para la entidad Libro.
 * El Controller nunca habla directo con el Repository: siempre pasa por
 * aqui, que valida las reglas del negocio antes de delegar la persistencia.
 */
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService() {
        this.libroRepository = new LibroRepositoryImpl();
    }

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> listarLibros() throws SQLException {
        return libroRepository.listarTodos();
    }

    public void registrarLibro(Libro libro) throws SQLException {
        validarLibro(libro);
        libroRepository.insertar(libro);
    }

    public void actualizarLibro(Libro libro) throws SQLException {
        validarLibro(libro);
        libroRepository.actualizar(libro);
    }

    public void eliminarLibro(String isbn) throws SQLException {
        libroRepository.eliminar(isbn);
    }

    public Libro buscarPorIsbn(String isbn) throws SQLException {
        return libroRepository.buscarPorIsbn(isbn);
    }

    /**
     * Reglas de negocio del catalogo bibliografico:
     * - El anio de publicacion debe estar entre 1450 (Gutenberg) y el anio actual.
     * - Las copias disponibles no pueden ser negativas.
     */
    private void validarLibro(Libro libro) {
        int anioActual = Year.now().getValue();
        if (libro.getAnioPublicacion() < 1450 || libro.getAnioPublicacion() > anioActual) {
            throw new IllegalArgumentException(
                    "El anio de publicacion debe estar entre 1450 y " + anioActual + ".");
        }
        if (libro.getCopiasDisponibles() < 0) {
            throw new IllegalArgumentException("Las copias disponibles no pueden ser negativas.");
        }
    }
}
