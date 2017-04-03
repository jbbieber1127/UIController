package controllers;

import definitions.HCProvider;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * Created by Leon Zhang on 2017/4/1.
 */
public class searchMenuController extends centralController implements Initializable {
  // define all ui elements
  @FXML
  private ListView SearchDirectory;
  @FXML
  private TextField SearchField;

  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    // actions of each ui element
  }

  public void updateDirectory (List<String> directory){
    ObservableList<String> listHC = FXCollections.observableList(directory);
    SearchDirectory.setItems(listHC);
  }

  public void back () {
    Stage primaryStage = (Stage) SearchDirectory.getScene().getWindow();
    try {
      startUI(primaryStage);
    } catch (Exception e) {
      System.out.println("Cannot load main menu");
      e.printStackTrace();
    }
  }

  public void clear () {
    SearchField.clear();
  }
}