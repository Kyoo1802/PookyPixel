package com.kyoo.pixel.connection.components;

import java.util.Optional;

public interface ConnectionComponent extends SelectableComponent {

  Optional<ConnectionComponent> getNextComponent();

  void setNextComponent(Optional<ConnectionComponent> component);

  Optional<ConnectionComponent> getPreviousComponent();

  void setPreviousComponent(Optional<ConnectionComponent> component);
}
