package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;

public final class Led implements ConnectionComponent {

  private static final Dimension LED_DIMENSION = new Dimension(1, 1);
  private final ConnectionComponent parentComponent;
  private Point position;

  public Led(Point position) {
    this(position, null);
  }

  public Led(Point position, ConnectionComponent parentComponent) {
    this.position = position;
    this.parentComponent = parentComponent;
  }

  public Point getIdxPosition() {
    return position;
  }

  @Override
  public boolean intersects(int x, int y) {
    return position.x == x && position.y == y;
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.LED_PATH;
  }

  @Override
  public long getId() {
    return 0;
  }

  @Override
  public Point getStartIdxPosition() {
    return null;
  }

  @Override
  public Point getEndIdxPosition() {
    return null;
  }

  @Override
  public Dimension getSize() {
    return LED_DIMENSION;
  }

  @Override
  public void addDimension(Dimension addDimension) {
  }

  @Override
  public ComponentSide scaleIntersection(int x, int y) {
    return ComponentSide.NONE;
  }

  @Override
  public String description() {
    return position.toString();
  }

  @Override
  public String toString() {
    return position.toString();
  }
}
