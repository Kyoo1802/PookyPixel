package com.kyoo.pixel.model;

import lombok.Data;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@Data
public final class PixelConnection {

  private List<PixelConnectionChannel> channels;

  public PixelConnection() {
    channels = new LinkedList<>();
  }

  public Point getStartPosition() {
    return new Point(getMinX(), getMinY());
  }

  private int getMinY() {
    int minY = Integer.MAX_VALUE;
    for (PixelConnectionChannel channel : channels) {
      for (Point point : channel.getConnectionPositions()) {
        minY = Math.min(minY, point.y);
      }
    }
    return minY == Integer.MAX_VALUE ? 0 : minY;
  }

  private int getMinX() {
    int minX = Integer.MAX_VALUE;
    for (PixelConnectionChannel channel : channels) {
      for (Point point : channel.getConnectionPositions()) {
        minX = Math.min(minX, point.x);
      }
    }
    return minX == Integer.MAX_VALUE ? 0 : minX;
  }

  private int getMaxX() {
    int maxX = 0;
    for (PixelConnectionChannel channel : channels) {
      for (Point point : channel.getConnectionPositions()) {
        maxX = Math.max(maxX, point.x);
      }
    }
    return maxX;
  }

  private int getMaxY() {
    int maxY = 0;
    for (PixelConnectionChannel channel : channels) {
      for (Point point : channel.getConnectionPositions()) {
        maxY = Math.max(maxY, point.y);
      }
    }
    return maxY;
  }

  public int getWidth() {
    return getMaxX() - getMinX() + 1;
  }

  public int getHeight() {
    return getMaxY() - getMinY() + 1;
  }
}
