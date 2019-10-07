package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.util.Optional;

public class LedBridge implements ConnectionComponent {

  public LedBridge(int idx, Point mousePoint) {
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
    return null;
  }

  public void endComponent(Point point) {

  }
}
