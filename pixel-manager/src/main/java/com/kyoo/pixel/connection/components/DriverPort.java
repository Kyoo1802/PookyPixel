package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Optional;
import lombok.Data;

@Data
public final class DriverPort implements ConnectionComponent {

  private long id;
  private Point idxPosition;
  private Point endPosition;
  private Dimension size;

  public DriverPort(long id, Point idxPosition, Dimension size) {
    this.id = id;
    this.idxPosition = idxPosition;
    this.size = size;
    this.endPosition = new Point(idxPosition.x + size.width - 1, idxPosition.y + size.height - 1);
  }

  @Override
  public boolean intersects(int x, int y) {
    return idxPosition.x <= x && x <= idxPosition.x + size.width && idxPosition.y <= y
        && y <= idxPosition.y + size.height;
  }

  @Override
  public boolean internalSelect(int x, int y) {
    return false;
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
  public ComponentType getComponentType() {
    return ComponentType.DRIVER_PORT;
  }


  @Override
  public CreationType getCreationType() {
    return CreationType.ONE_POINT;
  }

  @Override
  public Point getStartIdxPosition() {
    return idxPosition;
  }

  @Override
  public Point getEndIdxPosition() {
    return endPosition;
  }
}
