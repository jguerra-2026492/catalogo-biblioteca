package edu.netjags.catalogobiblioteca.digital.repository;

import edu.netjags.catalogobiblioteca.digital.model.Libro;

import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de acceso a datos para la entidad Libro.
 * Aisla al Service (y por lo tanto al Controller) de los detalles de JDBC.
 */
public interface LibroRepository {

    void insertar(Libro libro) throws SQLException;

    void actualizar(Libro libro) throws SQLException;

    void eliminar(String isbn) throws SQLException;

    Libro buscarPorIsbn(String isbn) throws SQLException;

    List<Libro> listarTodos() throws SQLException;
}
