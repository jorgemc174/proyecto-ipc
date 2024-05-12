/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vista;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Tooltip;

public class RegistroController implements Initializable {

    @FXML
    private TextField Rnombre;
    @FXML
    private TextField Rnickname;
    @FXML
    private TextField Rcontraseña;
    @FXML
    private TextField Rcorreo;
    @FXML
    private Button Login;
    @FXML
    private Button botonElegir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
private void VolverLogin(ActionEvent event) {
    // Verificar si los campos están llenos
    if (Rnombre.getText().isEmpty() || Rcorreo.getText().isEmpty() || Rnickname.getText().isEmpty() || Rcontraseña.getText().isEmpty() || !isValidEmail(Rcorreo.getText())) {
        // Mostrar un mensaje de error indicando qué campo falta
        String mensaje = "";
        if (Rnombre.getText().isEmpty()) {
            mensaje += "Completa Nombre\n";
        }
        if (Rnickname.getText().isEmpty()) {
            mensaje += "Completa Nickname\n";
        }
        if (Rcontraseña.getText().isEmpty()) {
            mensaje += "Completa Contraseña\n";
        }
        if (Rcorreo.getText().isEmpty()) {
            mensaje += "Completa Correo\n";
        } else if (!isValidEmail(Rcorreo.getText())) {
            mensaje += "Correo no válido\n";
            Rcorreo.setTooltip(new Tooltip("Formato de correo inválido"));
        }

        // Crear el Alert con mensaje personalizado en rojo
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Campos Incompletos o Incorrectos");
        alert.setContentText(mensaje);
        alert.showAndWait();
    } else {
        // Los campos están llenos, puedes volver al inicio de sesión
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login_Inicio.fxml"));
            Parent root = loader.load();

            // Acceder al controlador de Login_Inicio si es necesario
           
            // Usar el método setRoot de proyecto_ipc para establecer la raíz
            // Aquí asumo que proyecto_ipc es una clase estática con un método setRoot
            // Si no lo es, debes ajustarlo según tu implementación
            proyecto_ipc.setRoot(root);

            // Cerrar la ventana actual
            Stage stage = (Stage) Login.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
        }
    }
}

    @FXML
    private void ElegirFoto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/avatares.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
        }
    }
    public class proyecto_ipc {
    private static Parent root;

    public static void setRoot(Parent root) {
        proyecto_ipc.root = root;
    }

    public static Parent getRoot() {
        return root;
    }
    }
    private boolean isValidEmail(String email) {
    // Patrón para validar el formato de un correo electrónico
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();

}
}
