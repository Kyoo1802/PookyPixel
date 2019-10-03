package com.kyoo.pixel.data;

import java.awt.Point;
import java.util.List;
import lombok.Data;

@Data
public class PixelConnection {

  private int width;
  private int height;
  private List<PixelConnectionChannel> channels;

  public Point toFramePosition(Point visualizerPoint) {
    return new Point(visualizerPoint.x - getX(), visualizerPoint.y - getY());
  }

  private int getY() {
    int minY = Integer.MAX_VALUE;
    for (PixelConnectionChannel channel : channels) {
      for (Point point : channel.getLedPositions()) {
        minY = Math.min(minY, point.y);
      }
    }
    return minY == Integer.MAX_VALUE ? 0 : minY;
  }

  private int getX() {
    int minX = Integer.MAX_VALUE;
    for (PixelConnectionChannel channel : channels) {
      for (Point point : channel.getLedPositions()) {
        minX = Math.min(minX, point.x);
      }
    }
    return minX == Integer.MAX_VALUE ? 0 : minX;
  }
}
