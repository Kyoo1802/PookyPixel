package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;
import lombok.Data;

@Data
public final class DriverPort implements ConnectionComponent {

  private long id;
  private Point idxPosition;
  private Point endPosition;

  public DriverPort(long id, Point idxPosition, Dimension size) {
    this.id = id;
    this.idxPosition = idxPosition;
    this.endPosition = new Point(idxPosition.x + size.width - 1, idxPosition.y + size.height - 1);
  }

  @Override
  public boolean intersects(int x, int y) {
    Dimension size = getSize();
    return idxPosition.x <= x && x <= idxPosition.x + size.width && idxPosition.y <= y
        && y <= idxPosition.y + size.height;
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.DRIVER_PORT;
  }

  @Override
  public Point getStartIdxPosition() {
    return idxPosition;
  }

  @Override
  public Point getEndIdxPosition() {
    return endPosition;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(endPosition.x - idxPosition.x + 1, endPosition.y - idxPosition.y + 1);
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
    int w = getEndIdxPosition().x - getStartIdxPosition().x + 1;
    int h = getEndIdxPosition().y - getStartIdxPosition().y + 1;
    return String.format("[%d, %d] = %d", w, h, w * h);
  }
}
