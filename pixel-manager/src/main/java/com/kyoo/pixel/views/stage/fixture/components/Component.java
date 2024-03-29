package com.kyoo.pixel.views.stage.fixture.components;

import java.util.Optional;

public interface Component extends ComponentUnit {

  Optional<ComponentKey> getPreviousComponentKey();

  void setPreviousComponentKey(Optional<ComponentKey> componentKey);

  Optional<ComponentKey> getNextComponentKey();

  void setNextComponentKey(Optional<ComponentKey> componentKey);

  int availableInputs();

  int availableOutputs();
}
