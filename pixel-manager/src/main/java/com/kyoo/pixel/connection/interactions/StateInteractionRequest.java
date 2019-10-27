package com.kyoo.pixel.connection.interactions;

import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class StateInteractionRequest {

  private ActionState state;
  private Optional<Boolean> boolValue;
  private Optional<Integer> intValue;


  public enum ActionState {
    DRAW_SQUARE_PANEL,
    DRAW_DRIVER_PORT,
    DRAW_LED_PATH,
    DRAW_CONNECTOR_PORT,
  }
}