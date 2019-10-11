package com.kyoo.pixel.views.connection;

import com.google.inject.Inject;
import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ToggleButton;
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
  public ConnectionView(ConnectionViewModel connectionViewModel,
      ConnectionCanvasRenderer connectionCanvasRenderer) {
    this.connectionViewModel = connectionViewModel;
    this.connectionCanvasRenderer = connectionCanvasRenderer;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Initialize events
    connectionCanvas.onMouseMovedProperty().set(e -> {
      Point mousePosition = new Point((int) e.getX(), (int) e.getY());
      connectionViewModel.getMousePosition().get()
          .setLocation(mousePosition.x, mousePosition.y);
      connectionViewModel.updateCursorPosition();
      log.debug("Mouse Moved: (%d, %d)", mousePosition.x, mousePosition.y);
    });
    connectionCanvas.onMouseClickedProperty().set(e -> {
      Point mousePosition = new Point((int) e.getX(), (int) e.getY());
      connectionViewModel.handleComponentConnection();
      log.debug("Mouse Clicked: (%d, %d)", mousePosition.x, mousePosition.y);
    });
    connectionCanvas.onKeyTypedProperty().set(e -> {
      log.debug("Key typed: %s", e.getSource());
    });

    // Initialize properties
    connectionViewModel.getCanvasWidth().set((int) connectionCanvas.getWidth());
    connectionCanvas.widthProperty().bindBidirectional(connectionViewModel.getCanvasWidth());
    connectionViewModel.getCanvasHeight().set((int) connectionCanvas.getHeight());
    connectionCanvas.heightProperty().bindBidirectional(connectionViewModel.getCanvasHeight());
    createPanelBtn.selectedProperty()
        .bindBidirectional(connectionViewModel.createPanelSelectedProperty());

    // Initialize animation Handler
    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        connectionCanvasRenderer.render(connectionCanvas);
      }
    };
    timer.start();
  }

}