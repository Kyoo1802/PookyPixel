package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Optional;

public class LedBridge implements ConnectionComponent {

  public LedBridge(long id, Point mousePoint) {
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
  public ComponentType getComponentType() {
    return null;
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
    return null;
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
    return null;
  }

  @Override
  public void addDimension(Dimension addDimension) { }

}
