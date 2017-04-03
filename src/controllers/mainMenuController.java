package controllers;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * Created by Leon Zhang on 2017/4/1.
 */

public class mainMenuController extends centralController implements Initializable {
  // define all ui elements
  @FXML
  private AnchorPane MainMenuPane;

  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
  }

  public void gotoMap () {
    Stage primaryStage = (Stage) MainMenuPane.getScene().getWindow();
    try {
      mapViewFlag = 3;
      loadScene(primaryStage, "../fxmls/MapScene.fxml");
    } catch (Exception e) {
      System.out.println("error");
    }
  }

  public void gotoSearch () {
    Stage primaryStage = (Stage) MainMenuPane.getScene().getWindow();
    try {
      loadScene(primaryStage, "../fxmls/SearchMenu.fxml");
    } catch (Exception e) {
    }
  }

  public void gotoAdmin () {
    Stage primaryStage = (Stage) MainMenuPane.getScene().getWindow();
    try {
      loadScene(primaryStage, "../fxmls/AdminLogin.fxml");
    } catch (Exception e) {
    }
  }

}
