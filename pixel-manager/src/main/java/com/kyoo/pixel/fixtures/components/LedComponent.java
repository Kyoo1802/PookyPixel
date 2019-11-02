package com.kyoo.pixel.fixtures.components;

import com.kyoo.pixel.fixtures.components.led.Led;
import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;

public abstract class LedComponent extends BasicComponent {

  @Getter
  protected Led firstLed;
  @Getter
  protected Led lastLed;
  @Getter
  protected Map<Point, Led> leds;

  protected LedComponent(long id, ComponentType componentType) {
    super(id, componentType);
    this.leds = new LinkedHashMap<>();
  }
}
