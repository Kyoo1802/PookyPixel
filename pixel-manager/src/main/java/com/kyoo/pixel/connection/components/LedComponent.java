package com.kyoo.pixel.connection.components;

import java.util.Optional;

public interface LedComponent {

  Led firstLed();

  Led lastLed();

  Optional<LedBridge> getStartBridge();

  void setStartBridge(Optional<LedBridge> bridge);

  Optional<LedBridge> getEndBridge();

  void setEndBridge(Optional<LedBridge> bridge);
}
