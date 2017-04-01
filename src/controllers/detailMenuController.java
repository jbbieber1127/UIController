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

  // define all ui elements
  @FXML
  private Button detailQuitButton; // Value injected by FXMLLoader


  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    // actions of each ui element
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

