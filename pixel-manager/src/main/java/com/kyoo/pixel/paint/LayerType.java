package com.kyoo.pixel.paint;

import lombok.Getter;

public enum LayerType {
  DISPLAY(1),
  COMPONENTS(2),
  SELECTION(3),
  INTERACTION(4),
  INTERACTIVE_COMPONENTS(5),
  POINTER(6),
  EVENTS(7);

  @Getter private int weight;

  LayerType(int weight) {
    this.weight = weight;
  }
}
