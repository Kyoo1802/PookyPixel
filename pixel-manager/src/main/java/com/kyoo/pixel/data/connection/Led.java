package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.util.Optional;

public class Led implements ConnectionComponent {

  private final ConnectionComponent parentComponent;

  public Led(Point position, ConnectionComponent parentComponent) {
    this.parentComponent = parentComponent;
  }

  public ConnectionComponent getParentComponent() {
    return parentComponent;
  }

  void addLed(Led led) {

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
    return ComponentType.LED_PATH;
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
