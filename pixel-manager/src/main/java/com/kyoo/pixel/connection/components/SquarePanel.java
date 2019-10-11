package com.kyoo.pixel.connection.components;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Optional;
import lombok.Data;

@Data
public final class SquarePanel implements ConnectionComponent {

  private Point startIdxPosition;
  private Point endIdxPosition;
  private LinkedHashMap<Point, Led> leds;
  private long id;

  public SquarePanel(long id, Point startIdxPosition, Point endIdxPosition) {
    this.id = id;
    this.leds = new LinkedHashMap<>();
    this.startIdxPosition = startIdxPosition;
    this.endIdxPosition = endIdxPosition;
    createLeds();
  }

  @Override
  public boolean intersects(int x, int y) {
    return startIdxPosition.x <= x && x <= endIdxPosition.x && startIdxPosition.y <= y
        && y <= endIdxPosition.y;
  }

  @Override
  public boolean internalSelect(int x, int y) {
    return leds.containsKey(new Point(x, y));
  }

  @Override
  public ComponentType getConnectionType() {
    return ComponentType.SQUARE_PANEL;
  }

  @Override
  public Optional<ConnectionComponent> internalIntersects(Point position) {
    return Optional.empty();
  }

  @Override
  public CreationType getCreationType() {
    return CreationType.TWO_POINTS;
  }

  public void createLeds() {
    for (int i = startIdxPosition.y; i <= endIdxPosition.y; i++) {
      for (int j = startIdxPosition.x; j <= endIdxPosition.x; j++) {
        Point ledPoint = new Point(j, i);
        this.leds.put(ledPoint, new Led(ledPoint, this));
      }
    }
  }

  public long getId() {
    return id;
  }

  public Point getStartIdxPosition() {
    return startIdxPosition;
  }

  public Point getEndIdxPosition() {
    return endIdxPosition;
  }
}
