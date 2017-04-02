package controllers;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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

  double mapX;
  double mapY;

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
//        mapImage.setViewport(new Rectangle2D(300,300,400,400));
  }

  private void initializeMapImage() {
    mapX = mapImage.getX();
    mapY = mapImage.getY();
    mapImage.setFitHeight(mapImage.getImage().getHeight());
    mapImage.setFitWidth(mapImage.getImage().getWidth());
    mapImageWtHRatio = mapImage.getFitWidth() / mapImage.getFitHeight();
    System.out.println("Ratio is: " + mapImageWtHRatio);


  }

  private void moveMapImage(double x, double y) {
    mapX = x;
    mapY = y;
    mapImage.setX(x);
    mapImage.setY(y);
  }

  private void changeMapDimensions(double width, double height) {
    mapImage.setFitWidth(width);
    mapImage.setFitHeight(height);
  }

  private void initializeChoiceBox() {
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
            System.out.println(
                "You picked index: " + new_value + ". The old index was: " + old_value
                    + ". The value at that index is: " + floorChoiceBox.getItems()
                    .get((int) new_value));
            System.out
                .println("Changing floor to: " + floorChoiceBox.getItems().get((int) new_value));
            // Change the image that's being displayed when the input changes
            Image new_img = new Image(
                "/images/Floor Plans/" + (floorChoiceBox.getItems().get((int) new_value))
                    + "floor.png");
            mapImage.setImage(new_img);
          }
        });
    floorChoiceBox.setValue(1);
  }

  @FXML
  public void dragMap(MouseEvent e) {
    System.out.println("Dragging the Map");
    double newX = e.getSceneX() - difX;
    double newY = e.getSceneY() - difY;
    moveMapImage(newX, newY);
  }

  @FXML
  public void mapPressed(MouseEvent e) {
    System.out.println("Map Pressed");

    difX = e.getSceneX() - mapX;
    difY = e.getSceneY() - mapY;
  }

  @FXML
  public void mapReleased(javafx.event.Event e) {
  }

  @FXML
  public void mapScrolled(ScrollEvent e) {
    // Dimensions before resizing of image
    double oldHeight = mapImage.getFitHeight();
    double oldWidth = mapImage.getFitWidth();
    // Resize the Image
    mapImage.setFitHeight(mapImage.getFitHeight() + e.getDeltaY() * 2);
    mapImage.setFitWidth(mapImage.getFitHeight() * mapImageWtHRatio);
    // Difference in image sizes
    double difX = mapImage.getFitWidth() - oldWidth;
    double difY = mapImage.getFitHeight() - oldHeight;
    // Now move the image around the cursor, proportionally to its location
    double proportionX =
        (e.getSceneX() - mapImage.localToScene(mapImage.getBoundsInLocal()).getMinX()) / mapImage
            .getFitWidth();
    double proportionY =
        (e.getSceneY() - mapImage.localToScene(mapImage.getBoundsInLocal()).getMinY()) / mapImage
            .getFitHeight();
    moveMapImage(mapImage.getX() - difX * proportionX, mapImage.getY() - difY * proportionY);
    System.out.println("X: " + proportionX + " - Y: " + proportionY);

  }
}