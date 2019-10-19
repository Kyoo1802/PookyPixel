package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel.ConnectionState;
import com.kyoo.pixel.connection.ConnectionModel.TransformationAction;
import com.kyoo.pixel.connection.InputInteraction.KeyboardInteraction;
import com.kyoo.pixel.connection.InputInteraction.KeyboardState;
import com.kyoo.pixel.connection.InputInteraction.PositionInteraction;
import com.kyoo.pixel.connection.InputInteraction.PositionSide;
import com.kyoo.pixel.connection.InputInteraction.PositionState;
import com.kyoo.pixel.connection.InputInteraction.StateInteraction;
import com.kyoo.pixel.connection.components.commands.ConnectionCommand;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandManager;
import com.kyoo.pixel.connection.handlers.DrawingCommandHandler;
import com.kyoo.pixel.connection.handlers.SelectCommandHandler;
import com.kyoo.pixel.connection.handlers.TransformationHandler;
import com.kyoo.pixel.utils.PositionUtils;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
@Getter
public final class ConnectionViewModel {

  private ObjectProperty<ConcurrentLinkedQueue<InputInteraction>> inputInteractions =
      new SimpleObjectProperty<>(new ConcurrentLinkedQueue<>());
  private AtomicBoolean needsRender = new AtomicBoolean(true);

  private ConnectionModel model;
  private ConnectionCommandManager commandManager;
  private DrawingCommandHandler drawingCommandHandler;
  private SelectCommandHandler selectCommandHandler;
  private TransformationHandler transformationHandler;
  private Thread t;

  @Inject
  public ConnectionViewModel(ConnectionModel model, ConnectionCommandManager commandManager) {
    this.model = model;
    this.commandManager = commandManager;
    this.drawingCommandHandler = new DrawingCommandHandler(this);
    this.selectCommandHandler = new SelectCommandHandler(this);
    this.transformationHandler = new TransformationHandler(this);
    t = new Thread(() -> {
      while (true) {
        consumeInteractions();
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void start() {
    t.start();
  }

  public void consumeInteractions() {
    while (hasPendingInteractions()) {
      InputInteraction inputInteraction = inputInteractions.get().peek();
      log.debug("Consuming input interaction: " + inputInteraction);
      if (isStateInteraction(inputInteraction)) {
        handleStateInteraction((StateInteraction) inputInteraction);
      } else {
        handleActionInteraction(inputInteraction);
      }
      needsRender(true);
      inputInteractions.get().poll();
    }
  }

  public boolean needsRender(boolean v) {
    return needsRender.getAndSet(v);
  }

  public boolean hasPendingInteractions() {
    return !inputInteractions.get().isEmpty();
  }

  private void handleStateInteraction(StateInteraction interaction) {
    switch (interaction.getState()) {
      case DRAW_SQUARE_PANEL:
        model.setConnectionState(ConnectionState.DRAW_SQUARE_PANEL,
            interaction.getBoolValue().get());
        break;
      case DRAW_LED_PATH:
        model.setConnectionState(ConnectionState.DRAW_LED_PATH, interaction.getBoolValue().get());
        break;
      case DRAW_DRIVER_PORT:
        model
            .setConnectionState(ConnectionState.DRAW_DRIVER_PORT, interaction.getBoolValue().get());
        break;
      case DRAW_CONNECTOR_PORT:
        model
            .setConnectionState(ConnectionState.DRAW_LED_BRIDGE,
                interaction.getBoolValue().get());
        break;
      case RESIZE_WIDTH:
        model.getDimension().setSize(PositionUtils.toIdx(interaction.getIntValue().get()),
            model.getDimension().height);
        break;
      case RESIZE_HEIGHT:
        model.getDimension().setSize(model.getDimension().width,
            PositionUtils.toIdx(interaction.getIntValue().get()));
        break;
      default:
        log.error("Invalid interaction: " + interaction.getState());
    }
    model.setActiveCommandRequest(Optional.empty());
  }

  private void handleActionInteraction(InputInteraction interaction) {
    switch (model.getConnectionState()) {
      case NO_ACTION: {
        switch (getNoActionEvent(interaction)) {
          case DRAG:
          case POINTER_MOVE:
            model.handlePointerMovement(getPositionInteraction(interaction).getPosition());
            break;
          case PRESS:
            selectCommandHandler.handleSelectAction();
            transformationHandler.handleTransformation();
            break;
          case RELEASE:
            transformationHandler.handleTransformation();
            break;
          case CLICK:
            selectCommandHandler.handleSelectAction();
            model.setTransformationActionState(TransformationAction.UNSET);
            break;
          default:
            log.debug("Event not supported for NO_ACTION: " + interaction);
        }
      }
      break;
      case DRAW_DRIVER_PORT:
        switch (getDrawEvent(interaction)) {
          case FINISH_DRAW:
          case DRAW_POINT:
            drawingCommandHandler.handleDriverPortDrawing();
            break;
          case MOVE:
            model.handlePointerMovement(getPositionInteraction(interaction).getPosition());
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + interaction);
        }
        break;
      case DRAW_SQUARE_PANEL:
        switch (getDrawEvent(interaction)) {
          case FINISH_DRAW:
          case DRAW_POINT:
            drawingCommandHandler.handleSquarePanelDrawing();
            break;
          case MOVE:
            model.handlePointerMovement(getPositionInteraction(interaction).getPosition());
            break;
          case CANCEL:
            model.setActiveCommandRequest(Optional.empty());
            model.setSelectedComponent(Optional.empty());
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + interaction);
        }
        break;
      case DRAW_LED_BRIDGE:
        switch (getDrawEvent(interaction)) {
          case FINISH_DRAW:
          case DRAW_POINT:
            drawingCommandHandler.handleLedBridgeDrawing();
            break;
          case MOVE:
            model.handlePointerMovement(getPositionInteraction(interaction).getPosition());
            break;
          case CANCEL:
            model.setActiveCommandRequest(Optional.empty());
            model.setSelectedComponent(Optional.empty());
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + interaction);
        }
        break;
      case DRAW_LED_PATH:
        switch (getDrawEvent(interaction)) {
          case DRAW_POINT:
            drawingCommandHandler.handleLedPathDrawing(false);
            break;
          case MOVE:
            model.handlePointerMovement(getPositionInteraction(interaction).getPosition());
            break;
          case FINISH_DRAW:
            drawingCommandHandler.handleLedPathDrawing(true);
            break;
          case CANCEL:
            model.setActiveCommandRequest(Optional.empty());
            model.setSelectedComponent(Optional.empty());
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + interaction);
        }
        break;
      default:
        log.error("Invalid action to handle: " + model.getConnectionState());
    }
  }

  private DrawEvent getDrawEvent(InputInteraction interaction) {
    if (isPositionInteraction(interaction)) {
      PositionInteraction positionInteraction = getPositionInteraction(interaction);
      switch (positionInteraction.getState()) {
        case CLICKED:
          return DrawEvent.DRAW_POINT;
        case MOVED:
          return DrawEvent.MOVE;
      }
    } else if (isKeyboardInteraction(interaction)) {
      KeyboardInteraction keyboardInteraction = getKeyboardInteraction(interaction);
      if (keyboardInteraction.getState() == KeyboardState.RELEASED) {
        switch (keyboardInteraction.getKey()) {
          case ENTER:
            return DrawEvent.FINISH_DRAW;
          case ESCAPE:
            return DrawEvent.CANCEL;
        }
      }
    }
    return DrawEvent.UNKNOWN;
  }


  private NoActionEvent getNoActionEvent(InputInteraction interaction) {
    if (isPositionInteraction(interaction)) {
      PositionInteraction positionInteraction = getPositionInteraction(interaction);
      if (positionInteraction.getState() == PositionState.MOVED) {
        return NoActionEvent.POINTER_MOVE;
      } else if (positionInteraction.getSide() != PositionSide.LEFT) {
        return NoActionEvent.UNKNOWN;
      }
      switch (positionInteraction.getState()) {
        case CLICKED:
          return NoActionEvent.CLICK;
        case PRESSED:
          return NoActionEvent.PRESS;
        case RELEASED:
          return NoActionEvent.RELEASE;
        case MOVED:
          return NoActionEvent.POINTER_MOVE;
        case DRAGGED:
          return NoActionEvent.DRAG;
      }
    }
    return NoActionEvent.UNKNOWN;
  }

  private PositionInteraction getPositionInteraction(InputInteraction interaction) {
    return (PositionInteraction) interaction;
  }

  private KeyboardInteraction getKeyboardInteraction(InputInteraction interaction) {
    return (KeyboardInteraction) interaction;
  }

  private boolean isKeyboardInteraction(InputInteraction interaction) {
    return interaction instanceof KeyboardInteraction;
  }

  private boolean isPositionInteraction(InputInteraction interaction) {
    return interaction instanceof PositionInteraction;
  }

  private boolean isStateInteraction(InputInteraction interaction) {
    return interaction instanceof StateInteraction;
  }

  public void executeCommand(ConnectionCommand command) {
    commandManager.execute(command);
  }

  enum DrawEvent {
    UNKNOWN, DRAW_POINT, MOVE, FINISH_DRAW, CANCEL,
  }

  enum NoActionEvent {
    UNKNOWN, POINTER_MOVE, PRESS, RELEASE, CLICK, DRAG, CANCEL,
  }

}
