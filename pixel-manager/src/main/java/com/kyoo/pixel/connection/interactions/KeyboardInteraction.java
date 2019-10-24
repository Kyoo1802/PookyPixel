package com.kyoo.pixel.connection.interactions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.handlers.DrawingCommandHandler;
import com.kyoo.pixel.connection.interactions.KeyboardInteractionRequest.KeyboardState;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class KeyboardInteraction implements InputInteraction {

  private final DrawingCommandHandler drawingCommandHandler;
  private final ConnectionModel model;

  @Inject
  public KeyboardInteraction(DrawingCommandHandler drawingCommandHandler, ConnectionModel model) {
    this.drawingCommandHandler = drawingCommandHandler;
    this.model = model;
  }

  public void handleInteraction(KeyboardInteractionRequest request) {
    switch (model.getConnectionState()) {
      case DRAW_DRIVER_PORT:
        switch (getDrawEvent(request)) {
          case FINISH_DRAW:
            drawingCommandHandler.handleDriverPortDrawing();
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + this);
        }
        break;
      case DRAW_SQUARE_PANEL:
        switch (getDrawEvent(request)) {
          case FINISH_DRAW:
            drawingCommandHandler.handleSquarePanelDrawing();
            break;
          case CANCEL:
            model.setActiveCommandRequest(Optional.empty());
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + this);
        }
        break;
      case DRAW_LED_BRIDGE:
        switch (getDrawEvent(request)) {
          case FINISH_DRAW:
            drawingCommandHandler.handleBridgeDrawing();
            break;
          case CANCEL:
            model.setActiveCommandRequest(Optional.empty());
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + this);
        }
        break;
      case DRAW_LED_PATH:
        switch (getDrawEvent(request)) {
          case FINISH_DRAW:
            drawingCommandHandler.handleLedPathDrawing(true);
            break;
          case CANCEL:
            model.setActiveCommandRequest(Optional.empty());
            break;
          default:
            log.debug("Event not supported for DRAW_DRIVER_PORT: " + this);
        }
        break;
      default:
        log.debug("Invalid Keyboard action to handle: " + model.getConnectionState());
    }
  }

  private DrawEvent getDrawEvent(KeyboardInteractionRequest request) {
    if (request.getState() == KeyboardState.RELEASED) {
      switch (request.getKey()) {
        case ENTER:
          return DrawEvent.FINISH_DRAW;
        case ESCAPE:
          return DrawEvent.CANCEL;
        default:
          log.debug("Not supported Key: " + request.getKey());
      }
    }
    return DrawEvent.UNKNOWN;
  }

}
