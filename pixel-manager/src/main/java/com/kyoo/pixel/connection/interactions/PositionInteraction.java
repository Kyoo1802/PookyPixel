package com.kyoo.pixel.connection.interactions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.handlers.CreateComponentHandler;
import com.kyoo.pixel.connection.handlers.SelectCommandHandler;
import com.kyoo.pixel.connection.handlers.TransformationHandler;
import com.kyoo.pixel.connection.interactions.PositionInteractionRequest.PositionButtonSide;
import com.kyoo.pixel.connection.interactions.PositionInteractionRequest.PositionState;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class PositionInteraction implements InputInteraction {

  private final CreateComponentHandler createComponentHandler;
  private final SelectCommandHandler selectCommandHandler;
  private final TransformationHandler transformationHandler;
  private final ConnectionModel model;

  @Inject
  public PositionInteraction(CreateComponentHandler createComponentHandler,
      SelectCommandHandler selectCommandHandler,
      TransformationHandler transformationHandler,
      ConnectionModel model) {
    this.createComponentHandler = createComponentHandler;
    this.selectCommandHandler = selectCommandHandler;
    this.transformationHandler = transformationHandler;
    this.model = model;
  }

  public void handleInteraction(PositionInteractionRequest request) {
    switch (model.getConnectionState()) {
      case NO_ACTION: {
        switch (getNoActionEvent(request)) {
          case DRAG:
          case POINTER_MOVE:
            model.handlePointerMovement(request.getPosition());
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
            break;
          default:
            log.debug("Event not supported: " + getNoActionEvent(request));
        }
      }
      break;
      case CREATE_DRIVER_PORT:
        switch (getCreateEvent(request)) {
          case CREATE:
            createComponentHandler.createDriverPort();
            break;
          case MOVE:
            model.handlePointerMovement(request.getPosition());
            break;
          default:
            log.debug("Event not supported: " + getCreateEvent(request));
        }
        break;
      case CREATE_SQUARE_PANEL:
        switch (getCreateEvent(request)) {
          case CREATE:
            createComponentHandler.createSquarePanel();
            break;
          case MOVE:
            model.handlePointerMovement(request.getPosition());
            break;
          default:
            log.debug("Event not supported: " + getCreateEvent(request));
        }
        break;
      case CREATE_LED_BRIDGE:
        switch (getCreateEvent(request)) {
          case CREATE:
            createComponentHandler.createBridgePort();
            break;
          case MOVE:
            model.handlePointerMovement(request.getPosition());
            break;
          default:
            log.debug("Event not supported: " + getCreateEvent(request));
        }
        break;
      case CREATE_LED_PATH:
        switch (getCreateEvent(request)) {
          case CREATE:
            createComponentHandler.createLedPath(false);
            break;
          case MOVE:
            model.handlePointerMovement(request.getPosition());
            break;
          case COMPLETE:
            createComponentHandler.createLedPath(true);
            break;
          default:
            log.debug("Event not supported: " + getCreateEvent(request));
        }
        break;
      default:
        log.error("Invalid Position action to handle: " + model.getConnectionState());
    }
  }

  private NoActionEvent getNoActionEvent(PositionInteractionRequest request) {
    if (request.getState() == PositionState.MOVED) {
      return NoActionEvent.POINTER_MOVE;
    } else if (request.getSide() != PositionButtonSide.LEFT) {
      return NoActionEvent.UNKNOWN;
    }
    switch (request.getState()) {
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
    return NoActionEvent.UNKNOWN;
  }

  private CreateEvents getCreateEvent(PositionInteractionRequest request) {
    switch (request.getState()) {
      case CLICKED:
        return CreateEvents.CREATE;
      case MOVED:
        return CreateEvents.MOVE;
    }
    return CreateEvents.UNKNOWN;
  }

}
