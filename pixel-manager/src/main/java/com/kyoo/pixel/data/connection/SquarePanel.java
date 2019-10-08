package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Optional;
import lombok.Data;

@Data
public final class SquarePanel implements ConnectionComponent {

  private Point startPosition;
  private Optional<Point> endPosition;
  private LinkedHashMap<Point, Led> leds;
  private int idx;

  public SquarePanel(int idx, Point point) {
    this.idx = idx;
    this.startPosition = point;
    this.leds = new LinkedHashMap<>();
    this.endPosition = Optional.empty();
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
    this.endPosition = Optional.of(endPoint);
    for (int i = startPosition.y; i <= endPoint.y; i++) {
      for (int j = startPosition.x; j <= endPoint.x; j++) {
        Point ledPoint = new Point(j, i);
        this.leds.put(ledPoint, new Led(ledPoint, this));
      }
    }
  }
}
