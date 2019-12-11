package com.kyoo.pixel.views.stage.fixture.interactions;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
public final class StateInteractionRequest {

  private ActionState state;
  private Optional<Boolean> boolValue;
  private Optional<Integer> intValue;

  public enum ActionState {
    DRAW_SQUARE_PANEL,
    DRAW_LED_PATH,
    DRAW_CONNECTOR_PORT,
  }
}
