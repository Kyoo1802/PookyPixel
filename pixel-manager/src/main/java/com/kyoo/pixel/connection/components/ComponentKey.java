package com.kyoo.pixel.connection.components;

import lombok.Getter;

@Getter
public final class ComponentKey {

  private long id;
  private ComponentType type;

  public ComponentKey(long id, ComponentType type) {
    this.id = id;
    this.type = type;
  }
}
