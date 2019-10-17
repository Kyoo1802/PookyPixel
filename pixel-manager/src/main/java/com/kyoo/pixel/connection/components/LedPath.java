package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.LinkedHashSet;
import lombok.Data;

@Data
public final class LedPath implements ConnectionComponent, LedComponent {

  private long id;
  private LinkedHashSet<Led> leds;
  private Point startPosition;
  private Led first;
  private Led last;
  private Dimension size;

  public LedPath(long id, Collection<Point> leds) {
    this.id = id;
    this.leds = new LinkedHashSet<>();
    Point minXY = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    Point maxXY = new Point(0, 0);
    for (Point p : leds) {
      minXY.x = Math.min(p.x, minXY.x);
      minXY.y = Math.min(p.y, minXY.y);
      maxXY.x = Math.max(p.x, maxXY.x);
      maxXY.y = Math.max(p.y, maxXY.y);
      Led led = new Led(p, this);
      if (first == null) {
        first = led;
      }
      last = led;
      this.leds.add(led);
    }
    size = new Dimension(maxXY.x - minXY.x, maxXY.y - maxXY.y);
  }

  @Override
  public boolean intersects(int x, int y) {
    for (Led led : leds) {
      if (led.intersects(x, y)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.LED_PATH;
  }

  @Override
  public Point getStartIdxPosition() {
    return first.getIdxPosition();
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public Point getEndIdxPosition() {
    return last.getIdxPosition();
  }

  @Override
  public Dimension getSize() {
    return size;
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
    return String.format("id: %d, size: %s ", id, size);
  }

  @Override
  public Led firstLed() {
    return first;
  }

  @Override
  public Led lastLed() {
    return last;
  }
}
