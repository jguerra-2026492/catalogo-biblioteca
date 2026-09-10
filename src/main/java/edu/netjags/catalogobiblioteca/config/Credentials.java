package edu.netjags.catalogobiblioteca.digital.config;

/**
 * Centraliza los datos de conexion hacia la base de datos MySQL.
 * Separado de DatabaseConnection para que las credenciales puedan
 * ajustarse (o migrarse a variables de entorno) sin tocar la logica
 * de conexion en si.
 */
public final class Credentials {

    private static final String URL =
            "jdbc:mysql://localhost:3306/biblioteca_universitaria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // <-- Ajustar segun su entorno

    private Credentials() {
        // Clase de constantes: no debe instanciarse.
    }

    public static String getUrl() {
        return URL;
    }

    public static String getUsuario() {
        return USUARIO;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}
