package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.util.Optional;

public interface ConnectionComponent {

  boolean intersects(int x, int y);

  boolean internalSelect(int x, int y);

  ComponentType connectionType();

  Optional<ConnectionComponent> internalIntersects(Point position);

  CreationType creationType();

  Point getStartPosition();

  Point getEndPosition();
}
