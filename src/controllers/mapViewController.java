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
  @FXML
  Pane mapPane;

  double difX = 0;
  double difY = 0;
  double mapImageWtHRatio;

  @FXML
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    initializeChoiceBox();
    mapPane.setBackground(new Background(
        new BackgroundFill(javafx.scene.paint.Color.BLACK, new CornerRadii(0),
            new Insets(0, 0, 0, 0))));
    initializeMapImage();
//        mapImage.setViewport(new Rectangle2D(900,300,400,400));
  }

  private void initializeMapImage() {
    mapImage.setTranslateZ(-5); // Push the map into the background
    mapImage.setFitHeight(mapImage.getImage().getHeight());
    mapImage.setFitWidth(mapImage.getImage().getWidth());
    mapImageWtHRatio = mapImage.getFitWidth() / mapImage.getFitHeight();
  }

  private void moveMapImage(double x, double y) {
    mapImage.setX(x);
    mapImage.setY(y);
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
  private void mapScrolled(ScrollEvent e) {
    // Resize the Image
    resizeImageByHeight(mapImage.getFitHeight()*(e.getDeltaY() > 0 ? 1.05 : 0.95));
  }
}