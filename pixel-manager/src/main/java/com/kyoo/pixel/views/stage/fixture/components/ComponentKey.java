package com.kyoo.pixel.views.stage.fixture.components;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class ComponentKey {

  private long id;
  private ComponentType type;

  public ComponentKey(long id, ComponentType type) {
    this.id = id;
    this.type = type;
  }

  public static ComponentKey from(long id, ComponentType componentType) {
    return new ComponentKey(id, componentType);
  }
}
