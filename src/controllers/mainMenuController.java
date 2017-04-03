package controllers;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * Created by Leon Zhang on 2017/4/1.
 */

public class mainMenuController extends centralController implements Initializable {
  // define all ui elements
  @FXML
  private Pane MainMenu;

  private Stage primaryStage = (Stage) MainMenu.getScene().getWindow();

  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    // actions of each ui element
  }

  public void gotoMap () {
    try {
      loadScene(primaryStage, "");
    } catch (Exception e) {

    }
  }

  public void gotoSearch () {

  }

  public void gotoAdmin () {

  }

}
