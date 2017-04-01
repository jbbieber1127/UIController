package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class centralController  {
  /*
  @param primaryStage: The main stage of the application
  Set the stage to the initial scene
   */
  public void startUI (Stage primaryStage) throws Exception {
    loadScene(primaryStage, "../fxmls/AdminLogin.fxml");
    primaryStage.setTitle("Faulkner Hospital Kiosk");
  }

  /*
  @param primaryStage: The main stage of the application
  @param fxmlpath: the file path of the fxml file to be loaded
  Set the stage to a scene by an fxml file
   */
  public void loadScene (Stage primaryStage, String fxmlpath) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource(fxmlpath));
    primaryStage.setScene(new Scene(root, 1280, 720));
    primaryStage.show();
  }

}