package edu.netjags.catalogobiblioteca.digital.controller;

import edu.netjags.catalogobiblioteca.digital.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

/**
 * Controlador del Menu Principal. Desde aqui se navega hacia el
 * modulo de administracion "Gestion de Libros".
 */
public class MenuPrincipalController {

    @FXML
    private void gestionarLibros(ActionEvent event) {
        try {
            SceneManager.cambiarEscena(event, "/view/GestionLibros.fxml", "Biblioteca Universitaria - Gestion de Libros");
        } catch (IOException e) {
            mostrarError("No se pudo abrir el modulo de Gestion de Libros.", e);
        }
    }

    @FXML
    private void salir(ActionEvent event) {
        SceneManager.cerrarVentana(event);
    }

    private void mostrarError(String mensaje, Exception e) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(mensaje);
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
}
