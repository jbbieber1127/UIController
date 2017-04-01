package controllers;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by Leon Zhang on 2017/4/1.
 */

public class detailMenuController extends  centralController implements Initializable {

  @FXML //  fx:id="myButton"
  private Button detailQuitButton; // Value injected by FXMLLoader
  @FXML
  private Button detailMapButton; // Value injected by FXMLLoader

  @Override // This method is called by the FXMLLoader when initialization is complete
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

    detailMapButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event){
       Stage stage = (Stage) detailMapButton.getScene().getWindow();
       try {
         loadScene(stage, "../fxmls/MapScene.fxml");
       } catch (Exception e) {
         System.out.println("Cannot load Scene");
         e.printStackTrace();
       }
      }
    });

    // initialize your logic here: all @FXML variables will have been injected
    detailQuitButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {

        Stage stage = (Stage) detailQuitButton.getScene().getWindow();
        try {
          loadScene(stage, "../fxmls/AdminLogin.fxml");
        } catch (Exception e) {
          System.out.println("Cannot load Scene");
        }

      }
    });
  }
}

