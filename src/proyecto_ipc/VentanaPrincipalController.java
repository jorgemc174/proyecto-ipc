/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto_ipc;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ariel
 */
public class VentanaPrincipalController implements Initializable {

    @FXML
    private AnchorPane panelDesplegable;
    @FXML
    private Button abrirPanel;
    
PanelDeslizante panelDeslizante ;
    @FXML
    private Button botonUserPrincipal;
    @FXML
    private MenuBar barraMenuPrincipal;
    @FXML
    private Menu menuInsertar;
    @FXML
    private MenuItem menuItemInsertGasto;
    @FXML
    private Menu menuEditar;
    @FXML
    private MenuItem editarGasto;
    @FXML
    private MenuItem editarUsuario;
    @FXML
    private Menu menuAyuda;
    @FXML
    private Button botoAddPrincipal;
    @FXML
    private Button botonDelPrincipal;
    @FXML
    private Button gastosGeneral;
    @FXML
    private Button categoriaGeneral;
    @FXML
    private Button botonAddFactura;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
   
         panelDeslizante = new PanelDeslizante(panelDesplegable);
         Image imagen = new Image("/vista/menu.png");
         Image imagen2 = new Image("/vista/usuarioPrincipal2.png");
         Image imagen3 = new Image("/vista/añadirGasto.png");
         Image imagen4 = new Image("/vista/eliminarGasto.png");

        // Asociar la imagen al botón usando ImageView
        ImageView imageView = new ImageView(imagen);
        ImageView imageViewUser = new ImageView(imagen2);
        ImageView imageViewAdd = new ImageView(imagen3);
        ImageView imageViewDel = new ImageView(imagen4);
        
       imageView.setFitHeight(32);
       imageView.setFitWidth(28);
       
       imageViewUser.setFitWidth(40);
       imageViewUser.setFitHeight(40);
       imageViewUser.setTranslateX(-8.3);
      
       imageViewAdd.setFitWidth(83);
       imageViewAdd.setFitHeight(83);
       
       imageViewDel.setFitWidth(55);
       imageViewDel.setFitHeight(55);
       imageViewDel.setTranslateY(-8);
       
        abrirPanel.setGraphic(imageView);
        botonUserPrincipal.setGraphic(imageViewUser);
        botoAddPrincipal.setGraphic(imageViewAdd);
        botonDelPrincipal.setGraphic(imageViewDel);
        
        
     

    }    

    @FXML
    private void desplegarPanel(ActionEvent event) {
        
        panelDeslizante.abrirPanel();
    }

    @FXML
    private void abrirOpcionesUsuario(ActionEvent event) {
    }

    @FXML
    private void añadirGastoNuevo(ActionEvent event) {
    }

    @FXML
    private void eliminarGasto(ActionEvent event) {
    }
    
    public class PanelDeslizante {
    private Pane panel = new AnchorPane();
    private TranslateTransition transition;

    public PanelDeslizante(Pane panel) {
        this.panel = panel;
        transition = new TranslateTransition(new javafx.util.Duration(500), panel);
      
       
        
    }
 
 
 
 
    public void abrirPanel() {
        
       //System.out.println(panelDesplegable.getTranslateX());
        
        if (panelDesplegable.getTranslateX()<0) {
            // Desplazar el Pane hacia la derecha (200 unidades)
           transition.setToX(0);
        } else {
            // Regresar el Pane a su posición inicial (0 unidades)
            transition.setToX(-200);
        }

        // Iniciar la animación
        transition.play();
    }
       
        //transition.playFromStart();
        
    }
    
    
}
    

