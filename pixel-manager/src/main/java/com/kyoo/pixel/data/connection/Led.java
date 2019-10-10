package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.util.Optional;

public class Led implements ConnectionComponent {

  private final ConnectionComponent parentComponent;
  private Point position;

  public Led(Point position, ConnectionComponent parentComponent) {
    this.position = position;
    this.parentComponent = parentComponent;
  }

  public Point getIdxPosition() {
    return position;
  }

  @Override
  public boolean intersects(int x, int y) {
    return false;
  }

  @Override
  public boolean internalSelect(int x, int y) {
    return false;
  }

  @Override
  public ComponentType getConnectionType() {
    return ComponentType.LED_PATH;
  }

  @Override
  public Optional<ConnectionComponent> internalIntersects(Point position) {
    return Optional.empty();
  }

  @Override
  public long getId() {
    return 0;
  }

  @Override
  public CreationType getCreationType() {
    return CreationType.ONE_POINT;
  }

  @Override
  public Point getStartIdxPosition() {
    return null;
  }

  @Override
  public Point getEndIdxPosition() {
    return null;
  }
}
