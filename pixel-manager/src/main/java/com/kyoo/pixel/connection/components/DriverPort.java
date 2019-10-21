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
  private Optional<Bridge> startBridge;

  public DriverPort(long id, Point idxPosition, Dimension size) {
    this.id = id;
    this.idxPosition = idxPosition;
    this.endPosition = new Point(idxPosition.x + size.width - 1, idxPosition.y + size.height - 1);
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public boolean intersects(int x, int y) {
    Dimension size = getSize();
    return idxPosition.x <= x && x <= idxPosition.x + size.width && idxPosition.y <= y
        && y <= idxPosition.y + size.height;
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
  public Optional<Bridge> getStartBridge() {
    return Optional.empty();
  }

  @Override
  public void setStartBridge(Bridge bridge) {
    startBridge = Optional.of(bridge);
  }

  @Override
  public Optional<Bridge> getEndBridge() {
    return Optional.empty();
  }

  @Override
  public void setEndBridge(Bridge bridge) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.DRIVER_PORT;
  }

  @Override
  public String description() {
    int w = getEndIdxPosition().x - getStartIdxPosition().x + 1;
    int h = getEndIdxPosition().y - getStartIdxPosition().y + 1;
    return String.format("[%d, %d] = %d", w, h, w * h);
  }
}
