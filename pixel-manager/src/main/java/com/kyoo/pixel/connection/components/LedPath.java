package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import lombok.Data;

@Data
public final class LedPath implements ConnectionComponent, LedComponent {

  private long id;
  private LinkedHashSet<Led> leds;
  private Led first;
  private Led last;
  private Point minPoint;
  private Point maxPoint;
  private Optional<LedBridge> startBridge;
  private Optional<LedBridge> endBridge;

  public LedPath(long id, Collection<Led> ledsInput) {
    this.id = id;
    this.leds = new LinkedHashSet<>();
    this.startBridge = Optional.empty();
    this.endBridge = Optional.empty();
    minPoint = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    maxPoint = new Point(0, 0);
    for (Led led : ledsInput) {
      Point p = led.getIdxPosition();
      minPoint.x = Math.min(p.x, minPoint.x);
      minPoint.y = Math.min(p.y, minPoint.y);
      maxPoint.x = Math.max(p.x, maxPoint.x);
      maxPoint.y = Math.max(p.y, maxPoint.y);
      Led newLed = new Led(p, this);
      if (first == null) {
        first = newLed;
      }
      last = newLed;
      this.leds.add(newLed);
    }
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
    return minPoint;
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public Point getEndIdxPosition() {
    return maxPoint;
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
    return String.format("id: %d, size: %s ", id, getSize());
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
