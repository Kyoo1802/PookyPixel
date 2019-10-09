package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public final class LedPath implements ConnectionComponent {

  private int idx;
  private List<Led> internalLeds;
  private Point startPosition;

  public LedPath(int idx, Point position) {
    this.idx = idx;
    this.startPosition = position;
    this.internalLeds = new LinkedList<>();
    this.internalLeds.add(new Led(position, this));
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
    return null;
  }

  @Override
  public Optional<ConnectionComponent> internalIntersects(Point position) {
    return Optional.empty();
  }

  @Override
  public CreationType creationType() {
    return CreationType.MULTI_POINT;
  }

  @Override
  public Point getEndPosition() {
    return null;
  }

  public void addLed(Led led) {

  }
}
