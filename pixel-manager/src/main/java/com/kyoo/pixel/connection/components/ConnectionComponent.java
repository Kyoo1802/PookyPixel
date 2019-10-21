package com.kyoo.pixel.connection.components;

import com.kyoo.pixel.connection.components.commands.SelectCommand;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ConnectionComponent extends SelectableComponent {

  Optional<Bridge> getStartBridge();

  Optional<Bridge> getEndBridge();

  void setStartBridge(@Nullable Bridge bridge);

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
