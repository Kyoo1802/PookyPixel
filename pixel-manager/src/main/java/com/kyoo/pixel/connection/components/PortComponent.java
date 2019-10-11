package com.kyoo.pixel.connection.components;

import java.awt.Point;
import java.util.Optional;

public final class PortComponent implements ConnectionComponent {

  private long id;

  public PortComponent(long id, Point position) {
    this.id = id;
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
    return null;
  }

  @Override
  public Optional<ConnectionComponent> internalIntersects(Point position) {
    return Optional.empty();
  }

  @Override
  public long getId() {
    return id;
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
