package edu.netjags.catalogobiblioteca.digital.repository;

import edu.netjags.catalogobiblioteca.digital.config.DatabaseConnection;
import edu.netjags.catalogobiblioteca.digital.model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion JDBC de LibroRepository contra MySQL.
 * Cada metodo abre su propia conexion/PreparedStatement mediante
 * try-with-resources para asegurar el cierre correcto de los recursos.
 */
public class LibroRepositoryImpl implements LibroRepository {

    private static final String SQL_INSERT =
            "INSERT INTO libro (isbn, titulo, autor, editorial, anio_publicacion, copias_disponibles) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE libro SET titulo = ?, autor = ?, editorial = ?, anio_publicacion = ?, copias_disponibles = ? " +
            "WHERE isbn = ?";

    private static final String SQL_DELETE =
            "DELETE FROM libro WHERE isbn = ?";

    private static final String SQL_SELECT_ONE =
            "SELECT * FROM libro WHERE isbn = ?";

    private static final String SQL_SELECT_ALL =
            "SELECT * FROM libro ORDER BY titulo ASC";

    @Override
    public void insertar(Libro libro) throws SQLException {
        try (Connection con = DatabaseConnection.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {

            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getAnioPublicacion());
            ps.setInt(6, libro.getCopiasDisponibles());
            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Ya existe un libro registrado con el ISBN " + libro.getIsbn() + ".", e);
        }
    }

    @Override
    public void actualizar(Libro libro) throws SQLException {
        try (Connection con = DatabaseConnection.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getEditorial());
            ps.setInt(4, libro.getAnioPublicacion());
            ps.setInt(5, libro.getCopiasDisponibles());
            ps.setString(6, libro.getIsbn());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se encontro ningun libro con el ISBN " + libro.getIsbn() + ".");
            }
        }
    }

    @Override
    public void eliminar(String isbn) throws SQLException {
        try (Connection con = DatabaseConnection.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {

            ps.setString(1, isbn);
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se encontro ningun libro con el ISBN " + isbn + ".");
            }
        }
    }

    @Override
    public Libro buscarPorIsbn(String isbn) throws SQLException {
        try (Connection con = DatabaseConnection.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ONE)) {

            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearLibro(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Libro> listarTodos() throws SQLException {
        List<Libro> libros = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libros.add(mapearLibro(rs));
            }
        }
        return libros;
    }

    private Libro mapearLibro(ResultSet rs) throws SQLException {
        return new Libro(
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("editorial"),
                rs.getInt("anio_publicacion"),
                rs.getInt("copias_disponibles")
        );
    }
}
