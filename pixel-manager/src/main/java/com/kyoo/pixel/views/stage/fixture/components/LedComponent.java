package com.kyoo.pixel.views.stage.fixture.components;

import com.kyoo.pixel.views.stage.fixture.components.led.Led;
import lombok.Getter;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class LedComponent extends BasicComponent {

  @Getter protected Led firstLed;
  @Getter protected Led lastLed;
  @Getter protected Map<Point, Led> leds;

  protected LedComponent(long id, ComponentType componentType) {
    super(id, componentType);
    this.leds = new LinkedHashMap<>();
  }
}
