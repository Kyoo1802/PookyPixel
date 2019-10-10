package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.util.Optional;

public interface ConnectionComponent {

  boolean intersects(int x, int y);

  boolean internalSelect(int x, int y);

  Optional<ConnectionComponent> internalIntersects(Point position);

  long getId();

  CreationType getCreationType();

  ComponentType getConnectionType();

  Point getStartIdxPosition();

  Point getEndIdxPosition();
}
