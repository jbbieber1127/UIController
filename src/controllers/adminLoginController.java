package controllers;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by Leon Zhang on 2017/4/1.
 */


public class adminLoginController extends centralController implements Initializable {

  // define all ui elements
  @FXML
  private Pane AdminLogin; // Value injected by FXMLLoader

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
  }

  public void login () {
    Stage primaryStage = (Stage) AdminLogin.getScene().getWindow();
    // credential check
    try {
      loadScene(primaryStage, "../fxmls/MapScene.fxml");
    } catch (Exception e) {
      System.out.println("Cannot load map view");
      e.printStackTrace();
    }
  }

  public void back () {
    Stage primaryStage = (Stage) AdminLogin.getScene().getWindow();
    try {
      startUI(primaryStage);
    } catch (Exception e) {
      System.out.println("Cannot load main menu");
      e.printStackTrace();
    }
  }

}