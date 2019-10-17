package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;

public interface ConnectionComponent {

  boolean intersects(int x, int y);

  long getId();

  ComponentType getComponentType();

  Point getStartIdxPosition();

  Point getEndIdxPosition();

  Dimension getSize();

  void addDimension(Dimension scale);

  ComponentSide scaleIntersection(int x, int y);

  String description();

  enum ComponentSide {
    NONE,
    UPPER,
    UPPER_LEFT,
    LEFT,
    UPPER_RIGHT,
    RIGHT,
    BOTTOM_LEFT,
    BOTTOM,
    BOTTOM_RIGHT
  }
}
