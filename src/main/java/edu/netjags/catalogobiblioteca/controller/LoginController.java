package edu.netjags.catalogobiblioteca.digital.controller;

import edu.netjags.catalogobiblioteca.digital.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controlador de la pantalla de inicio de sesion.
 *
 * Nota: el requerimiento de este escenario no define una entidad de
 * Usuarios, por lo que se implementa una validacion simplificada
 * (usuario/contrasenia fijos) unicamente para cumplir con el flujo
 * Login -> Menu -> CRUD exigido por la rubrica de evaluacion.
 */
public class LoginController {

    private static final String USUARIO_VALIDO = "admin";
    private static final String PASSWORD_VALIDA = "admin123";

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText() == null ? "" : txtUsuario.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Debe ingresar usuario y contrasenia.");
            return;
        }

        if (usuario.equals(USUARIO_VALIDO) && password.equals(PASSWORD_VALIDA)) {
            lblMensaje.setText("");
            abrirMenuPrincipal(event);
        } else {
            lblMensaje.setText("Usuario o contrasenia incorrectos.");
        }
    }

    private void abrirMenuPrincipal(ActionEvent event) {
        try {
            SceneManager.cambiarEscena(event, "/view/MenuPrincipal.fxml", "Biblioteca Universitaria - Menu Principal");
        } catch (IOException e) {
            mostrarError("No se pudo cargar el menu principal.", e);
        }
    }

    private void mostrarError(String mensaje, Exception e) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(mensaje);
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
}
