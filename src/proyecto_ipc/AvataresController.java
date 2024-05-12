/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vista;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author USUARIO
 */
public class AvataresController implements Initializable {

    @FXML
    private Button Bardilla;
    @FXML
    private Button Bjirafa;
    @FXML
    private Button Bpinguino;
    @FXML
    private Button Bdinosaurio;
    @FXML
    private Button Bgato;
    @FXML
    private Button Bconejo;
    @FXML
    private Button Bperro;
    @FXML
    private Button BperroGris;
    @FXML
    private Button Bmariquita;
    @FXML
    private Button BconejoBlanco;
    @FXML
    private Button BAbeja;
    @FXML
    private Button Btortuga;
    @FXML
    private Button Bcomadreja;
    @FXML
    private Button Bgallo;
    @FXML
    private Button Bloro;
    @FXML
    private Button Bciervo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void vueltaLogin() {
        // Obtener la referencia a la ventana actual
        Stage stage = (Stage) Bardilla.getScene().getWindow();
        // Cerrar la ventana
        stage.close();
    }
    
}

