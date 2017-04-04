package controllers;
import com.sun.javafx.scene.paint.GradientUtils;
import definitions.ListNodes;
import definitions.Node;
import java.awt.Dimension;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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

  // Used to maintain the map's ratio
  private double mapImageWtHRatio;

  // Controls how much the zoom changes at a time.
  private final double ZOOM_COEF = 0.05;
  private final int ZOOM_MIN = -25;
  private final int ZOOM_MAX = 20;
  private int current_zoom = 0;

  // Map Image positioning boundaries
  // the furthest to the right that the left side of the map image may move
  private double MAP_X_MIN = 150;
  // the furthest to the left that the right side of the map image may move
  private double MAP_X_MAX = 1300;
  // the furthest down that the top of the map image may move
  private double MAP_Y_MIN = 150;
  // the furthest up that the bottom of the map image may move
  private double MAP_Y_MAX = 750;

  // Difference in cursor location from map image location
  private double difX = 0;
  private double difY = 0;
  // Where the mouse drag was initiated
  private double mapPressedX;
  private double mapPressedY;
  // Where the mouse drag was released
  private double mapReleasedX;
  private double mapReleasedY;

  ////////////////////////
  // Administrator Data //
  ////////////////////////

  // TODO remove this
  // Temporary class for building the code
  private class Node1{
    double xCoord;
    double yCoord;
    ArrayList<Node1> connections;
    Node1(double xCoord, double yCoord){
      this.xCoord = xCoord;
      this.yCoord = yCoord;
    }
    Node1(){
      xCoord = 0;
      yCoord = 0;
    }
    void connectTo(Node1 node){
      node.connections.add(this);
      this.connections.add(node);
    }
    double getXCoord(){
      return xCoord;
    }
    double getYCoord(){
      return yCoord;
    }
  }

  // ArrayList of Nodes to maintain in memory
  private ArrayList<Node1> nodes = new ArrayList<Node1>();

  // List of nodes that are currently being displayed
  private ArrayList<javafx.scene.Node> currentlyDrawnNodes = new ArrayList<javafx.scene.Node>();

  // Line to provide visual feedback when drawing nodes
  Line connectionFeedbackLine = null;

  private double NODE_RADIUS = 10;
  private Color NODE_COLOR = new Color(1, 0, 0,1);



  // TODO List
  // Add a feature to change privilege modes
  // Add feature to display nodes relative to map -> DONE
  // Add feature to display node connections
  // Add a feature to enable adding of nodes
  // Add a feature to enable editing of nodes
  // Add a feature to allow right-click dragging to connect nodes

  //////////////////
  // FXML Methods //
  //////////////////

  // Initialization
  @FXML
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    System.out.println("Permissions: " + mapViewFlag);
    initializeChoiceBox();
    initializeMapImage();
//    setViewPrivileges(1);
    nodes.add(new Node1(250, 400));
    paintNodes();
  }

  // Add values to the floor selector, add a listener, and set its default value
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

  private void initializeMapImage() {
    //mapImage.setTranslateZ(-5); // Push the map into the background
    mapImage.setFitHeight(mapImage.getImage().getHeight());
    mapImage.setFitWidth(mapImage.getImage().getWidth());
    mapImageWtHRatio = mapImage.getFitWidth() / mapImage.getFitHeight();
  }


  /* Type of map to show
  1 for interactive map
  2 for directory map
  3 for admin map
 */
  private void setViewPrivileges(int level){
    mapViewFlag = level;
  }

  // Navigates back to the main menu
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

  @FXML
  private void dragMap(MouseEvent e) {
    double newX = e.getSceneX() - difX;
    double newY = e.getSceneY() - difY;
    moveMapImage(newX, newY);
  }

  @FXML
  private void mapPressed(MouseEvent e) {
    // Drag, or initiate connection of a node
    String buttonUsed = e.getButton().name();
    mapPressedX = e.getSceneX();
    mapPressedY = e.getSceneY();
    difX = mapPressedX - mapImage.getX();
    difY = mapPressedY - mapImage.getY();
  }

  @FXML
  private void mapReleased(MouseEvent e) {
    String buttonUsed = e.getButton().name();
    // Connect any node that qualifies
    mapReleasedX = e.getSceneX();
    mapReleasedY = e.getSceneY();
  }

  @FXML
  public void zoomIn(){
    changeZoom(true);
  }

  @FXML
  public void zoomOut(){
    changeZoom(false);
  }

  // "scrolled" means the scroll wheel. This method controls zooming with the scroll wheel.
  // It's also referenced for zooming with buttons.
  @FXML
  private void mapScrolled(ScrollEvent e) {
    changeZoom(e.getDeltaY() > 0);
    // Then update the tracking for cursor location vs image location
    // Prevents odd behavior when dragging and scrolling simultaneously
    mapPressedX = e.getSceneX();
    mapPressedY = e.getSceneY();
    difX = mapPressedX - mapImage.getX();
    difY = mapPressedY - mapImage.getY();
  }

  ///////////////////////////
  // Scene Control Methods //
  ///////////////////////////

  // Draw the Nodes on the map. This does not draw connections, just the nodes.
  private void paintNodes(){
    if(mapViewFlag != 3){
      return;
    }
   ObservableList<javafx.scene.Node> children = ((AnchorPane) mapImage.getParent()).getChildren();
   children.removeAll(currentlyDrawnNodes);
    for(int i = 0; i < nodes.size(); i++){
      // draws the circle on the right spot in the scene, based on where it should be on the map.
      // takes into account translation and zoom.
      Point where2Draw = pixelToPoint(new Point((int) nodes.get(i).getXCoord(), (int) nodes.get(i).getYCoord()));

      // creates the circle at the proper location.
      // size of the circle scales proportionally with the map zoom
      Circle c = new Circle(where2Draw.x, where2Draw.y, NODE_RADIUS*(Math.pow(1 + ZOOM_COEF, current_zoom)));

      // By adding these Circles at this index, I am making them get painted in a certain order
      // The goal is to have them get painted right after the map image, but before the rest
      // of the components on the screen. That way they will appear on top of the map, but behind
      // the other visual components. 2 is the magic number.
      children.add(2, c);

      // Make sure that we track this object
      currentlyDrawnNodes.add(c);
      // Now display it
      c.setVisible(true);
    }
  }

  // Changes the location of the map, such that it is within the maintained bounds
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
      // The map is too small, not sure what to do.
    }
    if(isLeft && isRight){
      mapImage.setX(x);
    }else if(!isLeft && isRight){
      mapImage.setX(MAP_X_MIN-mapImage.getLayoutX());
    }else if(isLeft && !isRight){
      mapImage.setX(MAP_X_MAX-mapImage.getFitWidth()-mapImage.getLayoutX());
    }else{
      // The map is too small, not sure what to do.
    }
    // If we have admin privileges, then we need to move the node displays as well
//    if(mapViewFlag == 3){
      paintNodes();
//    }
  }

  // Takes a new width for the image to be resized to. Height is set based on the ratio.
  private void resizeImageByWidth(double width) {
    mapImage.setFitWidth(width);
    mapImage.setFitHeight(width/mapImageWtHRatio);
  }

  // Takes a new height for the image to be resized to. Width is set based on the ratio.
  private void resizeImageByHeight(double height){
    mapImage.setFitHeight(height);
    mapImage.setFitWidth(height*mapImageWtHRatio);
  }

  // Takes a point in the scene and returns the pixel on the map that corresponds
  public Point pointToPixel(Point p){
    double mapX = mapImage.getX() + mapImage.getLayoutX();
    double mapY = mapImage.getY() + mapImage.getLayoutY();
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

  // Takes the pixel on the map image and returns the position in the scene where it corresponds to.
  // Useful for drawing
  public Point pixelToPoint(Point p){
    // Determine how the scaling is currently set (zoom)
    double mapScale = mapImage.getFitWidth()/mapImage.getImage().getWidth();
    double actualX = p.x*mapScale + mapImage.getX() + mapImage.getLayoutX();
    double actualY = p.y*mapScale + mapImage.getY() + mapImage.getLayoutY();
    Point point = new Point((int) actualX, (int) actualY);
    return point;
  }

  // Either zooms in or out
  // if delta is true, the zoom increases
  // if delta is false, the zoom decreases
  // Also distributes spacing around the image, 50% of the difference in size on each side
  private void changeZoom(boolean delta){
    // Find the old dimensions, will be used for making the zooming look better
    double oldWidth = mapImage.getFitWidth();
    double oldHeight = mapImage.getFitHeight();
    // Resize the image
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
}