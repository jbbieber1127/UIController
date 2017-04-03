package controllers;
import com.sun.javafx.scene.paint.GradientUtils;
import java.awt.Dimension;
import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * Created by Leon Zhang on 2017/4/1.
 */
public class mapViewController extends centralController implements Initializable {
  // define all ui elements

  @FXML
  ImageView mapImage;
  @FXML
  ChoiceBox floorChoiceBox;

  private double difX = 0;
  private double difY = 0;
  private double mapImageWtHRatio;

  // Controls how much the zoom changes at a time.
  private final double ZOOM_COEF = 0.05;
  private final int ZOOM_MIN = -20;
  private final int ZOOM_MAX = 20;
  private int current_zoom = 0;

  // Map Image positioning boundaries
  // the furthest to the right that the left side of the map image may move
  private double MAP_X_MIN = 150;
  // the furthest to the left that the right side of the map image may move
  private double MAP_X_MAX = 1300;
  // the furthest down that the top of the map image may move
  private double MAP_Y_MIN = 150;
  // the furthest up that the bottom of the map image mage move
  private double MAP_Y_MAX = 750;

  @FXML
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    initializeChoiceBox();
    initializeMapImage();
  }

  @FXML
  public void back(){
    Stage primaryStage = (Stage) floorChoiceBox.getScene().getWindow();
    try {
      startUI(primaryStage);
    } catch (Exception e) {
      System.out.println("Cannot load main menu");
      e.printStackTrace();
    }
  }

  private void initializeMapImage() {
    mapImage.setTranslateZ(-5); // Push the map into the background
    mapImage.setFitHeight(mapImage.getImage().getHeight());
    mapImage.setFitWidth(mapImage.getImage().getWidth());
    mapImageWtHRatio = mapImage.getFitWidth() / mapImage.getFitHeight();
  }


  // Moves the map image somewhere on the scene. Makes sure that it does not go out of vision.
  private void moveMapImage(double x, double y) {
    // Make sure that the top of the map is above the minimum
    boolean isAbove = (y + mapImage.getLayoutY()) < MAP_Y_MIN;
    // Make sure that the bottom of the map is below the maximum
    boolean isBelow = (y + mapImage.getFitHeight() + mapImage.getLayoutY()) > MAP_Y_MAX;
    // Make sure that the left of the map is to the left of the minimum
    boolean isLeft = (x + mapImage.getLayoutX()) < MAP_X_MIN;
    // Make sure that the right of the map is to the right of the maximum
    boolean isRight = (x + mapImage.getFitWidth() + mapImage.getLayoutX()) > MAP_X_MAX;
    // Make the assertions, move the map
    if(isAbove && isBelow){
      mapImage.setY(y);
    }else if(!isAbove && isBelow){
      mapImage.setY(MAP_Y_MIN-mapImage.getLayoutY());
    }else if(isAbove && !isBelow){
      mapImage.setY(MAP_Y_MAX-mapImage.getFitHeight()-mapImage.getLayoutY());
    }else{
      // The map is too small, not sure what to do yet.
    }
    if(isLeft && isRight){
      mapImage.setX(x);
    }else if(!isLeft && isRight){
      mapImage.setX(MAP_X_MIN-mapImage.getLayoutX());
    }else if(isLeft && !isRight){
      mapImage.setX(MAP_X_MAX-mapImage.getFitWidth()-mapImage.getLayoutX());
    }else{
      // The map is too small, not sure what to do yet.
    }
  }

  private void resizeImageByWidth(double width) {
    mapImage.setFitWidth(width);
    mapImage.setFitHeight(width/mapImageWtHRatio);
  }

  private void resizeImageByHeight(double height){
    mapImage.setFitHeight(height);
    mapImage.setFitWidth(height*mapImageWtHRatio);
  }


  // Takes a point in the scene and returns the pixel on the map that corresponds
  public Point pointToPixel(Point p){
    double mapX = mapImage.getX();
    double mapY = mapImage.getY();
    double maxX = mapImage.getFitWidth() + mapX;
    double maxY = mapImage.getFitHeight() + mapY;
    if(p.x > mapX && p.x < maxX && p.y > mapY && p.y < maxY){
      double ratioX = (p.x - mapX)/mapImage.getFitWidth();
      double ratioY = (p.x - mapY)/mapImage.getFitHeight();
      double pixelX = ratioX*mapImage.getImage().getWidth();
      double pixelY = ratioY*mapImage.getImage().getHeight();
      return new Point((int) pixelX, (int) pixelY);
    }else{
      return null;
    }
  }

  private void initializeChoiceBox() {
    // Add options to change floors
    floorChoiceBox.getItems().add(1);
    floorChoiceBox.getItems().add(2);
    floorChoiceBox.getItems().add(3);
    floorChoiceBox.getItems().add(4);
    floorChoiceBox.getItems().add(5);
    floorChoiceBox.getItems().add(6);
    floorChoiceBox.getItems().add(7);
    // Add a ChangeListener to the floorChoiceBox
    floorChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
        new ChangeListener<Number>() {
          public void changed(ObservableValue ov, Number old_value, Number new_value) {
            // Change the image that's being displayed when the input changes
            Image new_img = new Image(
                "/images/floor_plans/" + (floorChoiceBox.getItems().get((int) new_value))
                    + "floor.png");
            mapImage.setImage(new_img);
          }
        });
    floorChoiceBox.setValue(4);
  }

  @FXML
  private void dragMap(MouseEvent e) {
//    System.out.println("Dragging the Map");
    double newX = e.getSceneX() - difX;
    double newY = e.getSceneY() - difY;
    moveMapImage(newX, newY);
  }

  @FXML
  private void mapPressed(MouseEvent e) {
    difX = e.getSceneX() - mapImage.getX();
    difY = e.getSceneY() - mapImage.getY();
  }

  @FXML
  private void mapReleased(javafx.event.Event e) {
  }

  @FXML
  public void zoomIn(){
    changeZoom(1);
  }

  @FXML
  public void zoomOut(){
    changeZoom(-1);
  }

  private void changeZoom(double zoom){
    // Find the old dimensions, will be used for making the zooming look better
    double oldWidth = mapImage.getFitWidth();
    double oldHeight = mapImage.getFitHeight();
    // Resize the image
    boolean delta = zoom > 0;
    if(delta){ // delta was positive
      if(current_zoom >= ZOOM_MAX){
        // Can't zoom any more
      } else {
        current_zoom++;
        resizeImageByHeight(mapImage.getFitHeight()*(1+ZOOM_COEF));
      }
    } else { // delta was negative
      if(current_zoom <= ZOOM_MIN) {
        // Can't zoom any more
      } else {
        current_zoom--;
        resizeImageByHeight(mapImage.getFitHeight()/(1 + ZOOM_COEF));
      }
    }
    // Find the new dimensions
    double newWidth = mapImage.getFitWidth();
    double newHeight = mapImage.getFitHeight();
    // Move the image so that the zoom appears to be in the center of the image
    moveMapImage(mapImage.getX() - (newWidth - oldWidth) / 2 ,mapImage.getY() - (newHeight - oldHeight) / 2);
  }

  // "scrolled" means the scroll wheel. This method controls zooming with the scroll wheel.
  // It's also referenced for zooming with buttons.
  @FXML
  private void mapScrolled(ScrollEvent e) {
    changeZoom(e.getDeltaY());
  }
}