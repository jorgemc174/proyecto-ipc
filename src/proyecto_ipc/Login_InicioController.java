/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto_ipc;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;import javafx.scene.Parent;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ariel
 */
public class Login_InicioController implements Initializable {

    @FXML
    private AnchorPane anchorIzq;
    @FXML
    private AnchorPane anchorDer;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField userName;
    @FXML
    private Label labelUser;
    @FXML
    private ImageView imageUser;
    @FXML
    private Label labelanalitica;
    @FXML
    private ImageView analitica;
    @FXML
    private Label labelPassword;
    @FXML
    private ImageView imagePassword;
    @FXML
    private Button botonIniciarSesion;
    @FXML
    private Hyperlink Registro;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    public void initialize() {
    botonIniciarSesion.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VentanaPrincipal.fxml"));
            try {
                Parent root = loader.load();
                Proyecto_IPC.setRoot(root);
            } catch (IOException ex) {
                throw new RuntimeException(ex);

            }

        });
    }
    @FXML
    private void iniciar_sesion(ActionEvent event) {
    }
    @FXML
    void acceder_registro(ActionEvent event) throws IOException  {
        
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("/vista/Registro.fxml"));
        Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait(); 
                

    }
}
