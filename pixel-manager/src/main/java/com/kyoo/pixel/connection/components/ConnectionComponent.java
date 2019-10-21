package com.kyoo.pixel.connection.components;

import java.util.Optional;
import javax.annotation.Nullable;

public interface ConnectionComponent extends SelectableComponent {

  Optional<Bridge> getStartBridge();

  void setStartBridge(@Nullable Bridge bridge);

  Optional<Bridge> getEndBridge();

  void setEndBridge(@Nullable Bridge bridge);

  enum ComponentSide {
    NONE,
    UPPER,
    UPPER_LEFT,
    LEFT,
    UPPER_RIGHT,
    RIGHT,
    BOTTOM_LEFT,
    BOTTOM,
    BOTTOM_RIGHT
  }
}
