package com.kyoo.pixel.views.connection;

import com.google.inject.Inject;
import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConnectionView implements Initializable {

  @FXML
  private ToggleButton createPanelBtn;

  @FXML
  private Canvas connectionCanvas;

  private ConnectionViewModel connectionViewModel;
  private ConnectionCanvasRenderer connectionCanvasRenderer;

  @Inject
  public ConnectionView(ConnectionViewModel connectionViewModel) {
    this.connectionViewModel = connectionViewModel;
    this.connectionCanvasRenderer = new ConnectionCanvasRenderer(connectionViewModel);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    SceneMouseHandler mouseHandler = new SceneMouseHandler();
    connectionCanvas.addEventHandler(MouseEvent.MOUSE_MOVED, mouseHandler);
    connectionCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);

    SceneKeyboardHandler keyHandler = new SceneKeyboardHandler();
    connectionCanvas.addEventHandler(KeyEvent.KEY_TYPED, keyHandler);

    connectionViewModel.canvasWidthProperty().set((int)connectionCanvas.getWidth());
    connectionCanvas.widthProperty().bindBidirectional(connectionViewModel.canvasWidthProperty());

    connectionViewModel.canvasHeightProperty().set((int)connectionCanvas.getHeight());
    connectionCanvas.heightProperty().bindBidirectional(connectionViewModel.canvasHeightProperty());

    createPanelBtn.selectedProperty()
        .bindBidirectional(connectionViewModel.createPanelSelectedProperty());

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        connectionCanvasRenderer.render(connectionCanvas);
      }
    };
    timer.start();
  }

  final class SceneMouseHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent e) {
      Point mousePosition = new Point((int) e.getX()-2, (int) e.getY()-2);

      if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
//        log.debug("Mouse Move: " + mousePosition.getX() + " , " + mousePosition.getY());
        connectionViewModel.positionProperty().get().setLocation(e.getX(), e.getY());
        connectionViewModel.updateCursorPosition(mousePosition);

      } else if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
//        log.debug("Mouse Clicked: " + mousePosition.getX() + " , " + mousePosition.getY());
        connectionViewModel.handleComponentConnection(mousePosition);
      }
    }
  }

  final class SceneKeyboardHandler implements EventHandler<KeyEvent> {

    @Override
    public void handle(KeyEvent e) {
      if (e.getEventType() == KeyEvent.KEY_TYPED) {
        log.debug("KeyTyped: " + e.getSource());
      }
    }
  }
}