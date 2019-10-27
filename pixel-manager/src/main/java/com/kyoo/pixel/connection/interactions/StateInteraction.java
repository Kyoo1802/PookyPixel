package com.kyoo.pixel.connection.interactions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionModel.ConnectionState;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class StateInteraction implements InputInteraction {

  private final ConnectionModel model;

  @Inject
  public StateInteraction(ConnectionModel model) {
    this.model = model;
  }

  public void handleInteraction(StateInteractionRequest request) {
    switch (request.getState()) {
      case DRAW_SQUARE_PANEL:
        model.setConnectionState(ConnectionState.CREATE_SQUARE_PANEL, request.getBoolValue().get());
        break;
      case DRAW_LED_PATH:
        model.setConnectionState(ConnectionState.CREATE_LED_PATH, request.getBoolValue().get());
        break;
      case DRAW_DRIVER_PORT:
        model
            .setConnectionState(ConnectionState.CREATE_DRIVER_PORT, request.getBoolValue().get());
        break;
      case DRAW_CONNECTOR_PORT:
        model
            .setConnectionState(ConnectionState.CREATE_LED_BRIDGE, request.getBoolValue().get());
        break;
      default:
        log.error("Invalid interaction: " + request.getState());
    }
    model.setActiveCommandRequest(Optional.empty());

  }
}
