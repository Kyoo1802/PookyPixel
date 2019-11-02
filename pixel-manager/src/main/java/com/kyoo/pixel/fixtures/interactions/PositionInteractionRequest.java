package com.kyoo.pixel.fixtures.interactions;

import java.awt.Point;
import javafx.scene.input.MouseButton;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class PositionInteractionRequest {

  private PositionState state;
  private PositionButtonSide side;
  private Point position;

  public enum PositionButtonSide {
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

  public enum PositionState {
    MOVED,
    CLICKED,
    DRAGGED,
    PRESSED,
    RELEASED,
  }
}