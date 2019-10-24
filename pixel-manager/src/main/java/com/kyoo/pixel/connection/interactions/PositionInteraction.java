package com.kyoo.pixel.connection.interactions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionModel.TransformationAction;
import com.kyoo.pixel.connection.handlers.DrawingCommandHandler;
import com.kyoo.pixel.connection.handlers.SelectCommandHandler;
import com.kyoo.pixel.connection.handlers.TransformationHandler;
import com.kyoo.pixel.connection.interactions.PositionInteractionRequest.PositionButtonSide;
import com.kyoo.pixel.connection.interactions.PositionInteractionRequest.PositionState;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class PositionInteraction implements InputInteraction {

  private final DrawingCommandHandler drawingCommandHandler;
  private final SelectCommandHandler selectCommandHandler;
  private final TransformationHandler transformationHandler;
  private final ConnectionModel model;

  @Inject
  public PositionInteraction(DrawingCommandHandler drawingCommandHandler,
      SelectCommandHandler selectCommandHandler,
      TransformationHandler transformationHandler,
      ConnectionModel model) {
    this.drawingCommandHandler = drawingCommandHandler;
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
            model.setTransformationAction(TransformationAction.UNSET);
            break;
          default:
            log.debug("Event not supported for NO_ACTION: " + this);
        }
      }
      break;
      case DRAW_DRIVER_PORT:
        switch (getDrawEvent(request)) {
          case DRAW_POINT:
            drawingCommandHandler.handleDriverPortDrawing();
            break;
          case MOVE:
            model.handlePointerMovement(request.getPosition());
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + this);
        }
        break;
      case DRAW_SQUARE_PANEL:
        switch (getDrawEvent(request)) {
          case DRAW_POINT:
            drawingCommandHandler.handleSquarePanelDrawing();
            break;
          case MOVE:
            model.handlePointerMovement(request.getPosition());
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + this);
        }
        break;
      case DRAW_LED_BRIDGE:
        switch (getDrawEvent(request)) {
          case DRAW_POINT:
            drawingCommandHandler.handleBridgeDrawing();
            break;
          case MOVE:
            model.handlePointerMovement(request.getPosition());
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + this);
        }
        break;
      case DRAW_LED_PATH:
        switch (getDrawEvent(request)) {
          case DRAW_POINT:
            drawingCommandHandler.handleLedPathDrawing(false);
            break;
          case MOVE:
            model.handlePointerMovement(request.getPosition());
            break;
          case FINISH_DRAW:
            drawingCommandHandler.handleLedPathDrawing(true);
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + this);
        }
        break;
      default:
        log.error("Invalid action to handle: " + model.getConnectionState());
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

  private DrawEvent getDrawEvent(PositionInteractionRequest request) {
    switch (request.getState()) {
      case CLICKED:
        return DrawEvent.DRAW_POINT;
      case MOVED:
        return DrawEvent.MOVE;
    }
    return DrawEvent.UNKNOWN;
  }

}
