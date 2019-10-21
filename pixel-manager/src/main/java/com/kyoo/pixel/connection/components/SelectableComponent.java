package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;

public interface SelectableComponent {

  long getId();

  boolean intersects(int x, int y);

  Point getStartIdxPosition();

  Point getEndIdxPosition();

  Dimension getSize();

  ComponentType getComponentType();

  String description();
}
