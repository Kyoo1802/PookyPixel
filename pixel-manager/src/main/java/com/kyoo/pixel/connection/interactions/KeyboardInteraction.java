package com.kyoo.pixel.connection.interactions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.interactions.KeyboardInteractionRequest.KeyboardState;
import com.kyoo.pixel.connection.interactions.handlers.CreateComponentHandler;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class KeyboardInteraction implements InputInteraction {

  private final CreateComponentHandler createComponentHandler;
  private final ConnectionModel model;

  @Inject
  public KeyboardInteraction(CreateComponentHandler createComponentHandler, ConnectionModel model) {
    this.createComponentHandler = createComponentHandler;
    this.model = model;
  }

  public void handleInteraction(KeyboardInteractionRequest request) {
    switch (model.getConnectionState()) {
      case CREATE_DRIVER_PORT:
        switch (getCreateEvent(request)) {
          case COMPLETE:
            createComponentHandler.createDriverPort();
          default:
            log.debug("Event not supported: " + getCreateEvent(request));
        }
        break;
      case CREATE_SQUARE_PANEL:
        switch (getCreateEvent(request)) {
          case COMPLETE:
            createComponentHandler.createSquarePanel();
            break;
          case CANCEL:
            createComponentHandler.cancel();
            break;
          default:
            log.debug("Event not supported: " + getCreateEvent(request));
        }
        break;
      case CREATE_LED_BRIDGE:
        switch (getCreateEvent(request)) {
          case COMPLETE:
            createComponentHandler.createBridgePort();
            break;
          case CANCEL:
            createComponentHandler.cancel();
            break;
          default:
            log.debug("Event not supported: " + getCreateEvent(request));
        }
        break;
      case CREATE_LED_PATH:
        switch (getCreateEvent(request)) {
          case COMPLETE:
            createComponentHandler.createLedPath(true);
            break;
          case CANCEL:
            createComponentHandler.cancel();
            break;
          default:
            log.debug("Event not supported: " + getCreateEvent(request));
        }
        break;
      default:
        log.debug("Invalid Keyboard action to handle: " + model.getConnectionState());
    }
  }

  private CreateEvents getCreateEvent(KeyboardInteractionRequest request) {
    if (request.getState() == KeyboardState.RELEASED) {
      switch (request.getKey()) {
        case ENTER:
          return CreateEvents.COMPLETE;
        case ESCAPE:
          return CreateEvents.CANCEL;
        default:
          log.debug("Not supported Key: " + request.getKey());
      }
    }
    return CreateEvents.UNKNOWN;
  }

}
