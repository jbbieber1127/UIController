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

public class detailMenuController extends  centralController implements Initializable {
  // define all ui elements
  @FXML
  private Pane DetailMenu; // Value injected by FXMLLoader

  private Stage primaryStage = (Stage) DetailMenu.getScene().getWindow();

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
  }

  public void quit () {
    try {
      startUI(primaryStage);
    } catch (Exception e) {
      System.out.println("Cannot load main menu");
      e.printStackTrace();
    }
  }

  public void back () {
    try {
      loadScene(primaryStage, "../fxmls/SearchMenu.fxml");
    } catch (Exception e) {
      System.out.println("Cannot load search menu");
      e.printStackTrace();
    }
  }

  public void gotoMap () {
    try {
      loadScene(primaryStage, "../fxmls/MapScene.fxml");
    } catch (Exception e) {
      System.out.println("Cannot load map view");
      e.printStackTrace();
    }
  }

}

