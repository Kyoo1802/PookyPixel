package com.kyoo.pixel.views.stage.fixture.interactions;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
public final class InteractionRequest {

  @Builder.Default
  private Optional<KeyboardInteractionRequest> keyboardInteractionRequest = Optional.empty();

  @Builder.Default
  private Optional<PositionInteractionRequest> positionInteractionRequest = Optional.empty();

  @Builder.Default
  private Optional<StateInteractionRequest> stateInteractionRequest = Optional.empty();

  public InteractionRequestType type() {
    if (keyboardInteractionRequest.isPresent()) {
      return InteractionRequestType.KEYBOARD;
    } else if (positionInteractionRequest.isPresent()) {
      return InteractionRequestType.POSITION;
    } else if (stateInteractionRequest.isPresent()) {
      return InteractionRequestType.STATE;
    } else {
      return InteractionRequestType.UNKNOWN;
    }
  }

  public enum InteractionRequestType {
    UNKNOWN,
    KEYBOARD,
    POSITION,
    STATE
  }
}
