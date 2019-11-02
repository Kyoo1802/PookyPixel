package com.kyoo.pixel.fixtures.interactions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.fixtures.FixtureModel;
import com.kyoo.pixel.fixtures.interactions.KeyboardInteractionRequest.KeyboardState;
import com.kyoo.pixel.fixtures.interactions.handlers.CreateComponentHandler;
import com.kyoo.pixel.fixtures.interactions.handlers.TransformationHandler;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class KeyboardInteraction implements InputInteraction {

  private final CreateComponentHandler createComponentHandler;
  private final TransformationHandler transformationHandler;
  private final FixtureModel model;

  @Inject
  public KeyboardInteraction(FixtureModel model, CreateComponentHandler createComponentHandler,
      TransformationHandler transformationHandler) {
    this.model = model;
    this.createComponentHandler = createComponentHandler;
    this.transformationHandler = transformationHandler;
  }

  public void handleInteraction(KeyboardInteractionRequest request) {
    switch (model.getStateAction()) {
      case NO_ACTION:
        switch (getNoActionEvent(request)) {
          case COMPLETE:
            transformationHandler.endTransformation();
            break;
          case CANCEL:
            transformationHandler.cancel();
            break;
          default:
            log.debug("Event not supported: " + getNoActionEvent(request));
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
        log.debug("Invalid Keyboard action to handle: " + model.getStateAction());
    }
  }

  private NoActionEvent getNoActionEvent(KeyboardInteractionRequest request) {
    if (request.getState() != KeyboardState.RELEASED) {
      return NoActionEvent.UNKNOWN;
    }
    switch (request.getKey()) {
      case ENTER:
        return NoActionEvent.COMPLETE;
      case ESCAPE:
        return NoActionEvent.CANCEL;
      default:
        return NoActionEvent.UNKNOWN;
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
