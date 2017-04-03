package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class centralController  {
  /* Type of map to show when mapView is displayed
    1 for interactive map
    2 for directory map
    3 for admin map
   */
  protected int mapViewFlag = 0;
  /*
  @param primaryStage: The main stage of the application
  Set the stage to the initial scene
   */
  public void startUI (Stage primaryStage) throws Exception {
    loadScene(primaryStage, "../fxmls/MainMenu.fxml");
    primaryStage.setTitle("Faulkner Hospital Kiosk");
  }

  /*
  @param primaryStage: The main stage of the application
  @param fxmlpath: the file path of the fxml file to be loaded
  Set the stage to a scene by an fxml file
   */
  public void loadScene (Stage primaryStage, String fxmlpath) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource(fxmlpath));
    primaryStage.setScene(new Scene(root, 1300, 750));
    primaryStage.show();
  }

}
