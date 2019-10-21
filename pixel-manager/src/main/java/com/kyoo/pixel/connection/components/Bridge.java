package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;

public class Bridge implements SelectableComponent {

  private long id;
  private ConnectionComponent startComponent;
  private ConnectionComponent endComponent;

  public Bridge(long id, ConnectionComponent startComponent, ConnectionComponent endComponent) {
    this.id = id;
    this.startComponent = startComponent;
    this.endComponent = endComponent;
  }

  @Override
  public boolean intersects(int x, int y) {
    return false;
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public Point getStartIdxPosition() {
    return startComponent.getStartIdxPosition();
  }

  @Override
  public Point getEndIdxPosition() {
    return endComponent.getEndIdxPosition();
  }

  @Override
  public Dimension getSize() {
    return new Dimension(getEndIdxPosition().x - getStartIdxPosition().x + 1,
        getEndIdxPosition().y - getStartIdxPosition().y + 1);
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.BRIDGE;
  }

  @Override
  public String description() {
    return String.format("EMPTY");
  }
}
