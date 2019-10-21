package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.kyoo.pixel.MainReleaseKeyHandler;
import com.kyoo.pixel.connection.InputInteractions.ActionState;
import com.kyoo.pixel.connection.InputInteractions.ActionStateInteractions;
import com.kyoo.pixel.connection.InputInteractions.KeyboardInteractions;
import com.kyoo.pixel.connection.InputInteractions.KeyboardKey;
import com.kyoo.pixel.connection.InputInteractions.KeyboardState;
import com.kyoo.pixel.connection.InputInteractions.PositionButtonSide;
import com.kyoo.pixel.connection.InputInteractions.PositionInteractions;
import com.kyoo.pixel.connection.InputInteractions.PositionState;
import java.awt.Point;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
  @FXML
  private ScrollPane canvasScroll;

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
    canvasScroll.setHbarPolicy(ScrollBarPolicy.ALWAYS);
    canvasScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    canvasScroll.setContent(canvas);

    // Initialize event listeners
    canvas.onMouseMovedProperty().set(e -> handleMouseInteraction(e, PositionState.MOVED));
    canvas.onMouseClickedProperty().set(e -> handleMouseInteraction(e, PositionState.CLICKED));
    canvas.onMousePressedProperty().set(e -> handleMouseInteraction(e, PositionState.PRESSED));
    canvas.onMouseReleasedProperty().set(e -> handleMouseInteraction(e, PositionState.RELEASED));
    canvas.onMouseDraggedProperty().set(e -> handleMouseInteraction(e, PositionState.DRAGGED));

    // Initialize properties
    handleStateInteraction((int) canvas.getWidth(), ActionState.RESIZE_WIDTH);
    handleStateInteraction((int) canvas.getHeight(), ActionState.RESIZE_HEIGHT);
    handleStateInteraction(createSquarePanelBtn.isSelected(), ActionState.DRAW_SQUARE_PANEL);
    handleStateInteraction(createLedPathBtn.isSelected(), ActionState.DRAW_LED_PATH);
    handleStateInteraction(createDriverPortBtn.isSelected(), ActionState.DRAW_DRIVER_PORT);
    handleStateInteraction(createBridgeBtn.isSelected(), ActionState.DRAW_CONNECTOR_PORT);

    // Initialize property listeners
    createSquarePanelBtn.selectedProperty()
        .addListener((observable, oldValue, newValue) -> handleStateInteraction(newValue,
            ActionState.DRAW_SQUARE_PANEL));
    createLedPathBtn.selectedProperty().addListener(
        (observable, oldValue, newValue) -> handleStateInteraction(newValue,
            ActionState.DRAW_LED_PATH));
    createBridgeBtn.selectedProperty().addListener(
        (observable, oldValue, newValue) -> handleStateInteraction(newValue,
            ActionState.DRAW_CONNECTOR_PORT));
    createDriverPortBtn.selectedProperty().addListener(
        (observable, oldValue, newValue) -> handleStateInteraction(newValue,
            ActionState.DRAW_DRIVER_PORT));

    // Initialize animation
    viewModel.start();
    canvasRenderer.render(canvas);
    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        if (viewModel.needsRender(false)) {
          canvasRenderer.render(canvas);
        }
      }
    };
    timer.start();
  }

  private void handleStateInteraction(Object value, ActionState actionState) {
    ActionStateInteractions interaction = new ActionStateInteractions();
    interaction.setActionState(actionState);
    if (value instanceof Boolean) {
      interaction.setBoolValue(Optional.of((Boolean) value));
    } else if (value instanceof Integer) {
      interaction.setIntValue(Optional.of((Integer) value));
    } else if (value instanceof Double) {
      interaction.setIntValue(Optional.of(((Double) value).intValue()));
    }
    viewModel.getInputInteractions().get().add(interaction);
    log.debug("ActionState [{}] on {}", actionState, interaction);
  }

  private void handleMouseInteraction(MouseEvent e, PositionState state) {
    PositionInteractions interaction = new PositionInteractions();
    interaction.setState(state);
    interaction.setPosition(new Point((int) e.getX(), (int) e.getY()));
    interaction.setSide(PositionButtonSide.from(e.getButton()));
    viewModel.getInputInteractions().get().add(interaction);
    log.debug("Mouse interaction: {}", interaction);
  }

  @Override
  public void handle(KeyEvent event) {
    KeyboardInteractions interaction = new KeyboardInteractions();
    interaction.setState(KeyboardState.RELEASED);
    interaction.setKey(KeyboardKey.from(event.getCode()));
    viewModel.getInputInteractions().get().add(interaction);
    log.debug("Keyboard interaction: {}", interaction);
  }
}