package com.kyoo.pixel.views.stage.fixture.components;

import java.awt.*;
import java.util.Map;

public interface ComponentUnit {

  ComponentKey getKey();

  Point getPosition();

  void setPosition(Point point);

  Dimension getSize();

  ComponentType type();

  Point getEndPosition();

  Map<String, Object> getProperties();

  boolean intersects(Point point);
}
