package com.kyoo.pixel.fixtures.components;

import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

public abstract class BasicComponent extends BasicComponentUnit implements Component {

  @Setter
  @Getter
  protected Optional<ComponentKey> previousComponentKey;
  @Setter
  @Getter
  protected Optional<ComponentKey> nextComponentKey;

  protected BasicComponent(long id, ComponentType componentType) {
    super(id, componentType);
    this.previousComponentKey = Optional.empty();
    this.nextComponentKey = Optional.empty();
  }

  @Override
  public int availableInputs() {
    return getPreviousComponentKey().isPresent() ? 0 : 1;
  }

  @Override
  public int availableOutputs() {
    return getNextComponentKey().isPresent() ? 0 : 1;
  }
}
