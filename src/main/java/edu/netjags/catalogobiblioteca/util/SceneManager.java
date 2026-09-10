package edu.netjags.catalogobiblioteca.digital.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Unico responsable de las llamadas de cambio de vista (navegacion).
 * Ningun controlador arma FXMLLoader/Scene/Stage por su cuenta: todos
 * pasan por aqui, para no repetir la misma logica de carga de FXML +
 * aplicacion del CSS en cada controlador.
 */
public final class SceneManager {

    private static final String CSS_PATH = "/css/biblioteca.css";

    private SceneManager() {
        // Clase de utilidad: no debe instanciarse.
    }

    /**
     * Muestra la primera escena de la aplicacion sobre un Stage nuevo
     * (se usa unicamente desde Main.start()).
     */
    public static void mostrarEscenaInicial(Stage stage, String fxmlPath, String titulo) throws IOException {
        Scene escena = construirEscena(fxmlPath);
        stage.setTitle(titulo);
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Reemplaza la escena del Stage actual (obtenido a partir del
     * ActionEvent del boton que disparo la navegacion) por la del
     * fxml indicado.
     */
    public static void cambiarEscena(ActionEvent event, String fxmlPath, String titulo) throws IOException {
        Scene escena = construirEscena(fxmlPath);
        Stage escenario = obtenerStage(event);
        escenario.setScene(escena);
        escenario.setTitle(titulo);
        escenario.centerOnScreen();
    }

    /**
     * Cierra la ventana actual (usado por ejemplo en la opcion "Salir"
     * del menu principal).
     */
    public static void cerrarVentana(ActionEvent event) {
        obtenerStage(event).close();
    }

    private static Scene construirEscena(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent raiz = loader.load();
        Scene escena = new Scene(raiz);
        escena.getStylesheets().add(SceneManager.class.getResource(CSS_PATH).toExternalForm());
        return escena;
    }

    private static Stage obtenerStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
