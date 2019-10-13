package com.kyoo.pixel.connection.components;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public final class LedPath implements ConnectionComponent {

  private long id;
  private List<Led> internalLeds;
  private Point startPosition;

  public LedPath(long id, Point position) {
    this.id = id;
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
  public ComponentType getComponentType() {
    return null;
  }

  @Override
  public Point getStartIdxPosition() {
    return null;
  }

  @Override
  public Optional<ConnectionComponent> internalIntersects(Point position) {
    return Optional.empty();
  }

  @Override
  public long getId() {
    return 0;
  }

  @Override
  public CreationType getCreationType() {
    return CreationType.MULTI_POINT;
  }

  @Override
  public Point getEndIdxPosition() {
    return null;
  }

  @Override
  public Dimension getSize() {
    return null;
  }

  @Override
  public void addDimension(Dimension addDimension) { }
}
