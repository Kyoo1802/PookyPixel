package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.kyoo.pixel.MainReleaseKeyHandler;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConnectionView implements Initializable, EventHandler<KeyEvent> {

  private final ConnectionViewModel viewModel;
  private final ConnectionCanvasRenderer canvasRenderer;
  private final MainReleaseKeyHandler keyboardHandler;
  @FXML
  private ToggleButton createSquarePanelBtn;
  @FXML
  private ToggleButton createLedPathBtn;
  @FXML
  private ToggleButton createDriverPortBtn;
  @FXML
  private ToggleButton createBridgeBtn;
  @FXML
  private Canvas canvas;

  @Inject
  public ConnectionView(ConnectionViewModel viewModel, ConnectionCanvasRenderer canvasRenderer,
      MainReleaseKeyHandler keyboardHandler) {
    this.viewModel = viewModel;
    this.canvasRenderer = canvasRenderer;
    this.keyboardHandler = keyboardHandler;
    this.keyboardHandler.attachListener(this);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    // Initialize event listeners
    canvas.setFocusTraversable(true);
    canvas.requestFocus();
    canvas.onMouseMovedProperty().set(e -> handleMouseInteraction(e, PositionState.MOVED));
    canvas.onMouseClickedProperty().set(e -> handleMouseInteraction(e, PositionState.CLICKED));
    canvas.onMousePressedProperty().set(e -> handleMouseInteraction(e, PositionState.PRESSED));
    canvas.onMouseReleasedProperty().set(e -> handleMouseInteraction(e, PositionState.RELEASED));
    canvas.onMouseDraggedProperty().set(e -> handleMouseInteraction(e, PositionState.DRAGGED));

    // Initialize properties
    handleStateInteraction((int) canvas.getWidth(), State.RESIZE_WIDTH);
    handleStateInteraction((int) canvas.getHeight(), State.RESIZE_HEIGHT);
    handleStateInteraction(createSquarePanelBtn.isSelected(), State.DRAW_SQUARE_PANEL);
    handleStateInteraction(createLedPathBtn.isSelected(), State.DRAW_LED_PATH);
    handleStateInteraction(createDriverPortBtn.isSelected(), State.DRAW_DRIVER_PORT);
    handleStateInteraction(createBridgeBtn.isSelected(), State.DRAW_CONNECTOR_PORT);

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
    createBridgeBtn.selectedProperty().addListener(
        (observable, oldValue, newValue) -> handleStateInteraction(newValue,
            State.DRAW_CONNECTOR_PORT));
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
    log.debug("Mouse interaction: {}", interaction);
  }

  @Override
  public void handle(KeyEvent event) {
    KeyboardInteraction interaction = new KeyboardInteraction();
    interaction.setState(KeyboardState.RELEASED);
    interaction.setKey(KeyboardKey.from(event.getCode()));
    viewModel.getInputInteractions().get().add(interaction);
    log.debug("Keyboard interaction: {}", interaction);

  }
}