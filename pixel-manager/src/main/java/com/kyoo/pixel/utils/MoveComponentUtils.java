package com.kyoo.pixel.utils;

import java.awt.Point;

public final class MoveComponentUtils {

  public static Point sumPoints(Point position, Point movement) {
    return new Point(position.x + movement.x, position.y + movement.y);
  }
}
