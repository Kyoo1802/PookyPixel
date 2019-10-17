package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;

public class ConnectionPort implements ConnectionComponent {

  private long id;
  private Point startIdxPosition;
  private Point endIdxPosition;
  public ConnectionPort(long id, Point startIdxPosition, Point endIdxPosition) {
    this.id = id;
    this.startIdxPosition = startIdxPosition;
    this.endIdxPosition = endIdxPosition;
  }

  @Override
  public boolean intersects(int x, int y) {
    return false;
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.CONNECTOR_PORT;
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public Point getStartIdxPosition() {
    return startIdxPosition;
  }

  @Override
  public Point getEndIdxPosition() {
    return endIdxPosition;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(endIdxPosition.x - startIdxPosition.x + 1,
        endIdxPosition.y - startIdxPosition.y + 1);
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
