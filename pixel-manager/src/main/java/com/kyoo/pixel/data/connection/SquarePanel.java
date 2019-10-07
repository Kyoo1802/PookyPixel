package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.util.Optional;
import lombok.Data;

@Data
public final class SquarePanel implements ConnectionComponent {

  private Point startPoint;
  private Point endPoint;
  private int idx;

  public SquarePanel(int idx, Point point) {
    this.idx = idx;
    this.startPoint = point;
    this.endPoint = point;
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
    return ComponentType.SQUARE_PANEL;
  }

  @Override
  public Optional<ConnectionComponent> internalIntersects(Point position) {
    return Optional.empty();
  }

  @Override
  public CreationType creationType() {
    return CreationType.TWO_POINTS;
  }

  public void endComponent(Point endPoint) {
    this.endPoint = endPoint;
  }
}
