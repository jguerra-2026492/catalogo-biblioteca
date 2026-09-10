package edu.netjags.catalogobiblioteca.digital.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Encargada unicamente de abrir y reutilizar la conexion JDBC.
 * Las credenciales viven aparte, en Credentials, para separar
 * "como me conecto" de "con que datos me conecto".
 */
public final class DatabaseConnection {

    private static Connection conexion;

    private DatabaseConnection() {
        // Clase de utilidad: no debe instanciarse.
    }

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(
                        Credentials.getUrl(), Credentials.getUsuario(), Credentials.getPassword());
            } catch (ClassNotFoundException e) {
                throw new SQLException(
                        "No se encontro el driver de MySQL (mysql-connector-j). "
                                + "Verifique que el JAR este agregado al classpath del proyecto.", e);
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
