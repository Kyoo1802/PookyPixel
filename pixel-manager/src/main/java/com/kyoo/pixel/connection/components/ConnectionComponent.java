package com.kyoo.pixel.connection.components;

import com.kyoo.pixel.connection.components.impl.Bridge;
import java.util.Optional;

public interface ConnectionComponent extends SelectableComponent {

  Optional<Bridge> getStartBridge();

  void setStartBridge(Optional<Bridge> bridge);

  Optional<Bridge> getEndBridge();

  void setEndBridge(Optional<Bridge> bridge);

  enum SelectedSide {
    NONE,
    CENTER,
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
