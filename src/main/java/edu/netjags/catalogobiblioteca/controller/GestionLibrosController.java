package edu.netjags.catalogobiblioteca.digital.controller;

import edu.netjags.catalogobiblioteca.digital.model.Libro;
import edu.netjags.catalogobiblioteca.digital.service.LibroService;
import edu.netjags.catalogobiblioteca.digital.util.SceneManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controlador del modulo "Gestion de Libros".
 * Implementa el CRUD completo (Crear, Leer, Actualizar, Borrar) sobre
 * la entidad Libro, delegando SIEMPRE al LibroService (nunca habla
 * directo con el Repository). El Controller solo se encarga de leer
 * los campos de la Vista y de mostrar el resultado; las reglas de
 * negocio (rangos validos, duplicados, etc.) viven en el Service.
 */
public class GestionLibrosController {

    @FXML private TextField txtIsbn;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtEditorial;
    @FXML private TextField txtAnio;
    @FXML private TextField txtCopias;

    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colAutor;
    @FXML private TableColumn<Libro, String> colEditorial;
    @FXML private TableColumn<Libro, Integer> colAnio;
    @FXML private TableColumn<Libro, Integer> colCopias;

    private final LibroService libroService = new LibroService();
    private final ObservableList<Libro> datosTabla = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));
        colCopias.setCellValueFactory(new PropertyValueFactory<>("copiasDisponibles"));

        tablaLibros.setItems(datosTabla);

        tablaLibros.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                cargarFormulario(seleccionado);
            }
        });

        cargarDatos();
    }

    private void cargarDatos() {
        try {
            datosTabla.setAll(libroService.listarLibros());
        } catch (SQLException e) {
            mostrarAlerta(AlertType.ERROR, "Error de base de datos", "No se pudieron cargar los libros.", e.getMessage());
        }
    }

    private void cargarFormulario(Libro libro) {
        txtIsbn.setText(libro.getIsbn());
        txtIsbn.setDisable(true); // El ISBN es la llave primaria: no se edita en modo actualizacion
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtEditorial.setText(libro.getEditorial());
        txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
        txtCopias.setText(String.valueOf(libro.getCopiasDisponibles()));
    }

    @FXML
    private void guardarLibro(ActionEvent event) {
        Libro libro = construirLibroDesdeFormulario();
        if (libro == null) {
            return;
        }
        try {
            libroService.registrarLibro(libro);
            mostrarAlerta(AlertType.INFORMATION, "Exito", "Libro registrado",
                    "El libro se guardo correctamente en el catalogo.");
            limpiarFormulario(null);
            cargarDatos();
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.WARNING, "Datos invalidos", "No se pudo registrar el libro.", e.getMessage());
        } catch (SQLException e) {
            mostrarAlerta(AlertType.ERROR, "Error al guardar", "No se pudo registrar el libro.", e.getMessage());
        }
    }

    @FXML
    private void actualizarLibro(ActionEvent event) {
        if (tablaLibros.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta(AlertType.WARNING, "Seleccion requerida", "Ningun libro seleccionado",
                    "Seleccione un libro de la tabla para actualizarlo.");
            return;
        }

        Libro libro = construirLibroDesdeFormulario();
        if (libro == null) {
            return;
        }

        try {
            libroService.actualizarLibro(libro);
            mostrarAlerta(AlertType.INFORMATION, "Exito", "Libro actualizado",
                    "Los datos del libro se actualizaron correctamente.");
            limpiarFormulario(null);
            cargarDatos();
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.WARNING, "Datos invalidos", "No se pudo actualizar el libro.", e.getMessage());
        } catch (SQLException e) {
            mostrarAlerta(AlertType.ERROR, "Error al actualizar", "No se pudo actualizar el libro.", e.getMessage());
        }
    }

    @FXML
    private void eliminarLibro(ActionEvent event) {
        Libro seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(AlertType.WARNING, "Seleccion requerida", "Ningun libro seleccionado",
                    "Seleccione un libro de la tabla para eliminarlo.");
            return;
        }

        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminacion");
        confirmacion.setHeaderText("Eliminar el libro \"" + seleccionado.getTitulo() + "\"?");
        confirmacion.setContentText("Esta accion no se puede deshacer.");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                try {
                    libroService.eliminarLibro(seleccionado.getIsbn());
                    mostrarAlerta(AlertType.INFORMATION, "Exito", "Libro eliminado",
                            "El libro se elimino del catalogo.");
                    limpiarFormulario(null);
                    cargarDatos();
                } catch (SQLException e) {
                    mostrarAlerta(AlertType.ERROR, "Error al eliminar", "No se pudo eliminar el libro.", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void limpiarFormulario(ActionEvent event) {
        txtIsbn.clear();
        txtIsbn.setDisable(false);
        txtTitulo.clear();
        txtAutor.clear();
        txtEditorial.clear();
        txtAnio.clear();
        txtCopias.clear();
        tablaLibros.getSelectionModel().clearSelection();
    }

    @FXML
    private void regresarMenu(ActionEvent event) {
        try {
            SceneManager.cambiarEscena(event, "/view/MenuPrincipal.fxml", "Biblioteca Universitaria - Menu Principal");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "No se pudo regresar al menu principal.", e.getMessage());
        }
    }

    /**
     * Lee y parsea los campos del formulario (validacion de FORMATO: campos
     * vacios, texto no numerico donde se espera un numero). Las reglas de
     * NEGOCIO (rangos validos de anio/copias, duplicados) las valida el
     * LibroService antes de persistir.
     */
    private Libro construirLibroDesdeFormulario() {
        String isbn = txtIsbn.getText() == null ? "" : txtIsbn.getText().trim();
        String titulo = txtTitulo.getText() == null ? "" : txtTitulo.getText().trim();
        String autor = txtAutor.getText() == null ? "" : txtAutor.getText().trim();
        String editorial = txtEditorial.getText() == null ? "" : txtEditorial.getText().trim();
        String anioTexto = txtAnio.getText() == null ? "" : txtAnio.getText().trim();
        String copiasTexto = txtCopias.getText() == null ? "" : txtCopias.getText().trim();

        if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty()
                || editorial.isEmpty() || anioTexto.isEmpty() || copiasTexto.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Datos incompletos", "Faltan campos por completar",
                    "Todos los campos son obligatorios.");
            return null;
        }

        if (isbn.length() > 13) {
            mostrarAlerta(AlertType.WARNING, "ISBN invalido", "El ISBN es demasiado largo",
                    "El ISBN no debe exceder 13 caracteres.");
            return null;
        }

        int anio;
        try {
            anio = Integer.parseInt(anioTexto);
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "Anio invalido", "El anio de publicacion debe ser numerico",
                    "Ingrese solo digitos, por ejemplo: 2020.");
            return null;
        }

        int copias;
        try {
            copias = Integer.parseInt(copiasTexto);
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "Cantidad invalida", "Las copias disponibles deben ser numericas",
                    "Ingrese solo digitos, por ejemplo: 5.");
            return null;
        }

        return new Libro(isbn, titulo, autor, editorial, anio, copias);
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
