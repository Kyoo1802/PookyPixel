package com.kyoo.pixel.fixtures.components;

import java.awt.Dimension;
import java.awt.Point;
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
