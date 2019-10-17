package com.kyoo.pixel.connection;

import java.awt.Point;
import java.util.Optional;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import lombok.Data;

public interface InputInteraction {

  enum State {
    UNKNOWN,
    DRAW_SQUARE_PANEL,
    DRAW_DRIVER_PORT,
    DRAW_LED_PATH,
    DRAW_CONNECTOR_PORT,
    RESIZE_WIDTH,
    RESIZE_HEIGHT,
  }

  enum PositionSide {
    UNKNOWN,
    LEFT,
    RIGHT;

    public static PositionSide from(MouseButton button) {
      switch (button) {
        case PRIMARY:
          return LEFT;
        case SECONDARY:
          return RIGHT;
        default:
          return UNKNOWN;
      }
    }
  }

  enum PositionState {
    UNKNOWN,
    MOVED,
    CLICKED,
    DRAGGED,
    PRESSED,
    RELEASED,
    ESCAPED,
  }

  enum KeyboardState {
    UNKNOWN,
    RELEASED;
  }

  enum KeyboardKey {
    UNKNOWN,
    ENTER;

    public static KeyboardKey from(KeyCode code) {
      switch (code) {
        case ENTER:
          return ENTER;
        default:
          return UNKNOWN;
      }
    }
  }

  @Data
  class StateInteraction implements InputInteraction {

    private State state;
    private Optional<Boolean> boolValue;
    private Optional<Integer> intValue;
  }

  @Data
  class PositionInteraction implements InputInteraction {

    private PositionState state;
    private PositionSide side;
    private Point position;
  }

  @Data
  class KeyboardInteraction implements InputInteraction {

    private KeyboardState state;
    private KeyboardKey key;
  }
}
