package com.kyoo.pixel.connection;

import java.awt.Point;
import java.util.Optional;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import lombok.Data;

public interface InputInteractions {

  enum ActionState {
    UNKNOWN,
    DRAW_SQUARE_PANEL,
    DRAW_DRIVER_PORT,
    DRAW_LED_PATH,
    DRAW_CONNECTOR_PORT,
    RESIZE_WIDTH,
    RESIZE_HEIGHT,
  }

  enum PositionButtonSide {
    UNKNOWN,
    LEFT,
    RIGHT;

    public static PositionButtonSide from(MouseButton button) {
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
        default:
          return UNKNOWN;
      }
    }
  }

  @Data
  class ActionStateInteractions implements InputInteractions {

    private ActionState actionState;
    private Optional<Boolean> boolValue;
    private Optional<Integer> intValue;
  }

  @Data
  class PositionInteractions implements InputInteractions {

    private PositionState state;
    private PositionButtonSide side;
    private Point position;
  }

  @Data
  class KeyboardInteractions implements InputInteractions {

    private KeyboardState state;
    private KeyboardKey key;
  }
}
