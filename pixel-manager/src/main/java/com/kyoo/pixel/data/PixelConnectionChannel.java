package com.kyoo.pixel.data;

import java.awt.Point;
import java.util.LinkedList;
import lombok.Data;

@Data
public final class PixelConnectionChannel {

  private int id;
  private LinkedList<Point> connectionPositions;

  public PixelConnectionChannel() {
    connectionPositions = new LinkedList<>();
  }
}
