package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;

public final class Led implements SelectableComponent {

  private static final Dimension LED_DIMENSION = new Dimension(1, 1);

  private Point position;

  public Led(Point position) {
    this(position, null);
  }

  public Led(Point position, ConnectionComponent parentComponent) {
    this.position = position;
  }

  @Override
  public long getId() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean intersects(int x, int y) {
    return position.x == x && position.y == y;
  }

  @Override
  public Point getStartIdxPosition() {
    return position;
  }

  @Override
  public Point getEndIdxPosition() {
    return position;
  }

  @Override
  public Dimension getSize() {
    return LED_DIMENSION;
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.LED;
  }

  @Override
  public String description() {
    return toString();
  }

  @Override
  public String toString() {
    return position.toString();
  }
}
