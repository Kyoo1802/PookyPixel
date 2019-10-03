package com.kyoo.pixel.data;

import java.awt.Point;
import java.util.Set;
import lombok.Data;

@Data
public final class PixelConnectionChannel {

  private int id;
  private Set<Point> ledPositions;
}
