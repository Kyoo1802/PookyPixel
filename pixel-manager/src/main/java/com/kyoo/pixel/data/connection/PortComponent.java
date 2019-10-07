package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.util.Optional;

public class PortComponent implements ConnectionComponent {


  public PortComponent(int idx, Point position) {
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
  public ComponentType connectionType() {
    return null;
  }

  @Override
  public Optional<ConnectionComponent> internalIntersects(Point position) {
    return Optional.empty();
  }

  @Override
  public CreationType creationType() {
    return CreationType.ONE_POINT;
  }
}
