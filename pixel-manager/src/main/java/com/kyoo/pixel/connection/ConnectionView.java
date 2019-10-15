package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.kyoo.pixel.connection.InputInteraction.KeyboardInteraction;
import com.kyoo.pixel.connection.InputInteraction.KeyboardKey;
import com.kyoo.pixel.connection.InputInteraction.KeyboardState;
import com.kyoo.pixel.connection.InputInteraction.PositionInteraction;
import com.kyoo.pixel.connection.InputInteraction.PositionSide;
import com.kyoo.pixel.connection.InputInteraction.PositionState;
import com.kyoo.pixel.connection.InputInteraction.State;
import com.kyoo.pixel.connection.InputInteraction.StateInteraction;
import java.awt.Point;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConnectionView implements Initializable {

  @FXML
  private ToggleButton createSquarePanelBtn;
  @FXML
  private ToggleButton createLedPathBtn;
  @FXML
  private ToggleButton createDriverPortBtn;
  @FXML
  private Canvas canvas;

  private ConnectionViewModel viewModel;
  private ConnectionCanvasRenderer canvasRenderer;

  @Inject
  public ConnectionView(ConnectionViewModel viewModel, ConnectionCanvasRenderer canvasRenderer) {
    this.viewModel = viewModel;
    this.canvasRenderer = canvasRenderer;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Initialize event listeners
    canvas.setFocusTraversable(true);
    canvas.onMouseMovedProperty().set(e -> handleMouseInteraction(e, PositionState.MOVED));
    canvas.onMouseClickedProperty().set(e -> handleMouseInteraction(e, PositionState.CLICKED));
    canvas.onMousePressedProperty().set(e -> handleMouseInteraction(e, PositionState.PRESSED));
    canvas.onMouseReleasedProperty().set(e -> handleMouseInteraction(e, PositionState.RELEASED));
    canvas.onMouseDraggedProperty().set(e -> handleMouseInteraction(e, PositionState.DRAGGED));
    canvas.onKeyReleasedProperty()
        .set(e -> handleKeyInteraction(e.getCode(), KeyboardState.RELEASED));

    // Initialize properties
    handleStateInteraction((int) canvas.getWidth(), State.RESIZE_WIDTH);
    handleStateInteraction((int) canvas.getHeight(), State.RESIZE_HEIGHT);
    handleStateInteraction(createSquarePanelBtn.isSelected(), State.DRAW_SQUARE_PANEL);
    handleStateInteraction(createLedPathBtn.isSelected(), State.DRAW_LED_PATH);
    handleStateInteraction(createDriverPortBtn.isSelected(), State.DRAW_DRIVER_PORT);

    // Initialize property listeners
    canvas.widthProperty()
        .addListener((observable, oldValue, newValue) -> handleStateInteraction(newValue,
            State.RESIZE_WIDTH));
    canvas.heightProperty()
        .addListener((observable, oldValue, newValue) -> handleStateInteraction(newValue,
            State.RESIZE_HEIGHT));
    createSquarePanelBtn.selectedProperty()
        .addListener((observable, oldValue, newValue) -> handleStateInteraction(newValue,
            State.DRAW_SQUARE_PANEL));
    createLedPathBtn.selectedProperty().addListener(
        (observable, oldValue, newValue) -> handleStateInteraction(newValue, State.DRAW_LED_PATH));
    createDriverPortBtn.selectedProperty().addListener(
        (observable, oldValue, newValue) -> handleStateInteraction(newValue,
            State.DRAW_DRIVER_PORT));

    // Initialize animation Handler
    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        canvasRenderer.render(canvas);
      }
    };
    timer.start();
  }

  private void handleStateInteraction(Object value, State state) {
    StateInteraction interaction = new StateInteraction();
    interaction.setState(state);
    if (value instanceof Boolean) {
      interaction.setBoolValue(Optional.of((Boolean) value));
    } else if (value instanceof Integer) {
      interaction.setIntValue(Optional.of((Integer) value));
    }
    viewModel.getInputInteractions().get().add(interaction);
    log.debug("State [{}] on {}", state, interaction);
  }

  private void handleMouseInteraction(MouseEvent e, PositionState state) {
    PositionInteraction interaction = new PositionInteraction();
    interaction.setState(state);
    interaction.setPosition(new Point((int) e.getX(), (int) e.getY()));
    interaction.setSide(PositionSide.from(e.getButton()));
    viewModel.getInputInteractions().get().add(interaction);
    log.debug("Mouse [{}] on {}", state, interaction);
  }

  private void handleKeyInteraction(KeyCode code, KeyboardState state) {
    KeyboardInteraction interaction = new KeyboardInteraction();
    interaction.setState(state);
    interaction.setKey(KeyboardKey.from(code));
    viewModel.getInputInteractions().get().add(interaction);
    log.debug("Keyboard [{}] on {}", state, code.getName());
  }
}