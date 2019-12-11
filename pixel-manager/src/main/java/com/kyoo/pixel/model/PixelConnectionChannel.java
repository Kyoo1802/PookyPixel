package com.kyoo.pixel.model;

import lombok.Data;

import java.awt.*;
import java.util.LinkedList;

@Data
public final class PixelConnectionChannel {

  private int id;
  private LinkedList<Point> connectionPositions;

  public PixelConnectionChannel() {
    connectionPositions = new LinkedList<>();
  }
}
