package com.kyoo.pixel.data;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public final class PixelConnectionChannel {

  private int id;
  private LinkedList<Point> connectionPositions;

  public PixelConnectionChannel() {
    connectionPositions = new LinkedList<>();
  }
}
