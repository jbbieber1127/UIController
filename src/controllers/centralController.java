package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class centralController  {
  public void startUI (Stage primaryStage) throws Exception {
    loadScene(primaryStage, "../fxmls/AdminLogin.fxml");
    primaryStage.setTitle("Faulkner Hospital Kiosk");
  }

  public void loadScene (Stage primaryStage, String fxmlpath) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource(fxmlpath));
    primaryStage.setScene(new Scene(root, 1280, 720));
    primaryStage.show();
  }

}
