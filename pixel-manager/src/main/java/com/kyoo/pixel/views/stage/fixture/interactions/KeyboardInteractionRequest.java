package com.kyoo.pixel.views.stage.fixture.interactions;

import javafx.scene.input.KeyCode;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class KeyboardInteractionRequest {

  private KeyboardState state;
  private KeyboardKey key;

  public enum KeyboardState {
    RELEASED;
  }

  public enum KeyboardKey {
    UNKNOWN,
    ENTER,
    ESCAPE,
    DELETE;

    public static KeyboardKey from(KeyCode code) {
      switch (code) {
        case ENTER:
          return ENTER;
        case ESCAPE:
          return ESCAPE;
        case DELETE:
          return DELETE;
      }
      return UNKNOWN;
    }
  }
}
