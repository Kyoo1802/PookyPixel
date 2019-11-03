package com.kyoo.pixel.fixtures;

import com.google.inject.Inject;
import com.kyoo.pixel.MainReleaseKeyHandler;
import com.kyoo.pixel.fixtures.interactions.InteractionRequest;
import com.kyoo.pixel.fixtures.interactions.KeyboardInteractionRequest;
import com.kyoo.pixel.fixtures.interactions.KeyboardInteractionRequest.KeyboardKey;
import com.kyoo.pixel.fixtures.interactions.KeyboardInteractionRequest.KeyboardState;
import com.kyoo.pixel.fixtures.interactions.PositionInteractionRequest;
import com.kyoo.pixel.fixtures.interactions.PositionInteractionRequest.PositionButtonSide;
import com.kyoo.pixel.fixtures.interactions.PositionInteractionRequest.PositionState;
import com.kyoo.pixel.fixtures.interactions.StateInteractionRequest;
import com.kyoo.pixel.fixtures.interactions.StateInteractionRequest.ActionState;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FixtureView implements Initializable, EventHandler<KeyEvent> {

  private final FixtureViewModel viewModel;
  private final FixtureCanvasRenderer canvasRenderer;
  private final MainReleaseKeyHandler keyboardHandler;
  @FXML
  private ToggleButton createSquarePanelBtn;
  @FXML
  private ToggleButton createLedPathBtn;
  @FXML
  private ToggleButton createBridgeBtn;
  @FXML
  private Canvas canvas;
  @FXML
  private ScrollPane canvasScroll;
  @FXML
  private TreeView<String> elements;
  @FXML
  private StackPane propertyBtns;
  @FXML
  private StackPane createBtns;
  @FXML
  private AnchorPane mainPane;

  @Inject
  public FixtureView(FixtureViewModel viewModel, FixtureCanvasRenderer canvasRenderer,
      MainReleaseKeyHandler keyboardHandler) {
    this.viewModel = viewModel;
    this.canvasRenderer = canvasRenderer;
    this.keyboardHandler = keyboardHandler;
    this.keyboardHandler.attachListener(this);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    mainPane.widthProperty().addListener((o, oV, nV) -> {
      AnchorPane.setLeftAnchor(propertyBtns, mainPane.getWidth() / 2 - propertyBtns.getWidth() / 2);
      AnchorPane.setLeftAnchor(createBtns, mainPane.getWidth() / 2 - createBtns.getWidth() / 2);
    });

    TreeItem<String> rootItem = new TreeItem<>("Pantalla");
    TreeItem<String> efectos = new TreeItem<>("Elementos");
    efectos.getChildren().add(new TreeItem<>("Matriz 10x20"));
    efectos.getChildren().add(new TreeItem<>("Matriz 30x40"));
    efectos.getChildren().add(new TreeItem<>("Matriz 10x10"));
    efectos.getChildren().add(new TreeItem<>("Tira Leds 30"));
    efectos.getChildren().add(new TreeItem<>("Tira Leds 45"));
    efectos.getChildren().add(new TreeItem<>("Tira Leds 23"));
    efectos.getChildren().add(new TreeItem<>("Estrella 3"));
    efectos.getChildren().add(new TreeItem<>("Esfera"));
    rootItem.getChildren().add(efectos);
    rootItem.setExpanded(true);
    elements.setShowRoot(true);
    elements.setRoot(rootItem);

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
    handleStateInteraction(createSquarePanelBtn.isSelected(), ActionState.DRAW_SQUARE_PANEL);
    handleStateInteraction(createLedPathBtn.isSelected(), ActionState.DRAW_LED_PATH);
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

    // Initialize animation
    viewModel.startConsumingInteraction();
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
    StateInteractionRequest.StateInteractionRequestBuilder stateInteractionBuilder =
        StateInteractionRequest
            .builder()
            .state(actionState);

    if (value instanceof Boolean) {
      stateInteractionBuilder
          .boolValue(Optional.of((Boolean) value));
    } else if (value instanceof Integer) {
      stateInteractionBuilder
          .intValue(Optional.of((Integer) value));
    } else if (value instanceof Double) {
      stateInteractionBuilder
          .intValue(Optional.of(((Double) value).intValue()));
    }

    InteractionRequest interaction =
        InteractionRequest
            .builder()
            .stateInteractionRequest(Optional.of(stateInteractionBuilder.build()))
            .build();
    viewModel.getInputInteractions().get().add(interaction);

    log.debug("ActionState [{}] on {}", actionState, interaction);
  }

  private void handleMouseInteraction(MouseEvent e, PositionState state) {
    InteractionRequest interaction =
        InteractionRequest
            .builder()
            .positionInteractionRequest(
                Optional.of(
                    PositionInteractionRequest
                        .builder()
                        .state(state)
                        .position(new Point((int) e.getX(), (int) e.getY()))
                        .side(PositionButtonSide.from(e.getButton()))
                        .build()))
            .build();
    viewModel.getInputInteractions().get().add(interaction);
    log.debug("Mouse interaction: {}", interaction);
  }

  @Override
  public void handle(KeyEvent event) {
    InteractionRequest interaction =
        InteractionRequest
            .builder()
            .keyboardInteractionRequest(
                Optional.of(
                    KeyboardInteractionRequest
                        .builder()
                        .state(KeyboardState.RELEASED)
                        .key(KeyboardKey.from(event.getCode()))
                        .build()))
            .build();
    viewModel.getInputInteractions().get().add(interaction);
    log.debug("Keyboard interaction: {}", interaction);
  }
}