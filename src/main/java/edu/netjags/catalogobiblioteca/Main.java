package edu.netjags.catalogobiblioteca.digital;

import edu.netjags.catalogobiblioteca.digital.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicacion JavaFX.
 * Carga la pantalla de Login como escena inicial, delegando la
 * navegacion al SceneManager.
 */
public class Main extends Application {

    @Override
    public void start(Stage stagePrincipal) throws Exception {
        SceneManager.mostrarEscenaInicial(
                stagePrincipal, "/view/login-view.fxml", "Biblioteca Universitaria - Catalogo Bibliografico");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
