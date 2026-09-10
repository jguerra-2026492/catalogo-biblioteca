package main.java.edu.netjags.catalogobiblioteca.digital;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicacion JavaFX.
 * Carga la pantalla de Login como escena inicial.
 */
public class Main extends Application {

    @Override
    public void start(Stage stagePrincipal) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/netjags/catalogobiblioteca/digital/view/Login.fxml"));
        Parent raiz = loader.load();

        Scene escena = new Scene(raiz);
        escena.getStylesheets().add(getClass().getResource("/edu/netjags/catalogobiblioteca/digital/view/biblioteca.css").toExternalForm());

        stagePrincipal.setTitle("Biblioteca Universitaria - Catalogo Bibliografico");
        stagePrincipal.setScene(escena);
        stagePrincipal.setResizable(false);
        stagePrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
