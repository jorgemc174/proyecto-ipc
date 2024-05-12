/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private void VolverLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login_Inicio.fxml"));
        Parent root = loader.load();

        // Acceder al controlador de Login_Inicio si es necesario
        LoginInicioController loginInicioController = loader.getController();
        // Usar el método setRoot de proyecto_ipc para establecer la raíz
        proyecto_ipc.setRoot(root);
    }

    @FXML
    private void ElegirFoto(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/avatares.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
}