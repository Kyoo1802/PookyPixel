package com.kyoo.pixel.connection;

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
  private ToggleButton createSquarePanelBtn;
  @FXML
  private ToggleButton createDriverPortBtn;
  @FXML
  private Canvas canvas;
  private ConnectionViewModel viewModel;
  private ConnectionCanvasRenderer canvasRenderer;

  @Inject
  public ConnectionView(ConnectionViewModel viewModel,
      ConnectionCanvasRenderer canvasRenderer) {
    this.viewModel = viewModel;
    this.canvasRenderer = canvasRenderer;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Initialize events
    canvas.onMouseMovedProperty().set(e -> {
      Point mousePosition = new Point((int) e.getX(), (int) e.getY());
      viewModel.getMousePosition().set(mousePosition);
      viewModel.updateCursorPosition();
      log.debug("Mouse Moved: %s", mousePosition);
    });
    canvas.onMouseClickedProperty().set(e -> {
      Point mousePosition = new Point((int) e.getX(), (int) e.getY());
      viewModel.handleComponentConnection();
      log.debug("Mouse Clicked: %s", mousePosition);
    });
    canvas.onKeyTypedProperty().set(e -> log.debug("Key typed: %s", e.getSource()));

    // Initialize properties
    viewModel.getCanvasWidth().bindBidirectional(canvas.widthProperty());
    viewModel.getCanvasHeight().bindBidirectional(canvas.heightProperty());
    viewModel.getCreateSquarePanelSelected()
        .bindBidirectional(createSquarePanelBtn.selectedProperty());
    viewModel.getCreateDriverPortSelected()
        .bindBidirectional(createDriverPortBtn.selectedProperty());

    // Initialize animation Handler
    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        canvasRenderer.render(canvas);
      }
    };
    timer.start();
  }

}