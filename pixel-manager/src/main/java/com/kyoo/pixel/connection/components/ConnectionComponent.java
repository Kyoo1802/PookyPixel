package com.kyoo.pixel.connection.components;

import com.kyoo.pixel.connection.components.impl.Bridge;
import java.util.Optional;

public interface ConnectionComponent extends SelectableComponent {

  Optional<Bridge> getStartBridge();

  void setStartBridge(Optional<Bridge> bridge);

  Optional<Bridge> getEndBridge();

  void setEndBridge(Optional<Bridge> bridge);

}
