package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;

public class LedBridge implements ConnectionComponent {

  private long id;
  private LedComponent startComponent;
  private LedComponent endComponent;

  public LedBridge(long id, LedComponent startIdxPosition, LedComponent endIdxPosition) {
    this.id = id;
    this.startComponent = startIdxPosition;
    this.endComponent = endIdxPosition;
  }

  @Override
  public boolean intersects(int x, int y) {
    return false;
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.LED_BRIDGE;
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public Point getStartIdxPosition() {
    return startComponent.lastLed().getIdxPosition();
  }

  @Override
  public Point getEndIdxPosition() {
    return endComponent.firstLed().getIdxPosition();
  }

  @Override
  public Dimension getSize() {
    return new Dimension(getEndIdxPosition().x - getStartIdxPosition().x + 1,
        getEndIdxPosition().y - getStartIdxPosition().y + 1);
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
    return null;
  }

}
