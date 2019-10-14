package com.kyoo.pixel.utils;

import java.awt.Dimension;
import java.awt.Point;

public final class PositionUtils {

  public static final int SQUARE_LENGTH = 20;
  public static final int HALF_SQUARE_LENGTH = SQUARE_LENGTH / 2;

  public static Point toIdxPosition(Point mousePosition) {
    int j = toIdx(mousePosition.x);
    int i = toIdx(mousePosition.y);
    return new Point(j, i);
  }

  public static Point toRoundPosition(Point mousePosition) {
    int mouseX2 = toStartCanvas(mousePosition.x);
    int mouseY2 = toStartCanvas(mousePosition.y);
    return new Point(mouseX2, mouseY2);
  }

  public static Point toCanvasPosition(Point idxPosition) {
    return toCanvasPosition(idxPosition.y, idxPosition.x);
  }

  public static Point toCanvasPosition(int i, int j) {
    int x = toCanvas(j);
    int y = toCanvas(i);
    return new Point(x, y);
  }

  private static int toStartCanvas(int xy) {
    int tmpXY = toIdx(xy);
    return tmpXY * SQUARE_LENGTH + HALF_SQUARE_LENGTH;
  }

  private static int toCanvas(int idx) {
    return SQUARE_LENGTH * idx + HALF_SQUARE_LENGTH;
  }

  private static int toIdx(int xy) {
    int tmpXY = Math.max(xy - HALF_SQUARE_LENGTH, 0);
    return tmpXY / SQUARE_LENGTH;
  }

  public static Dimension toCanvasDimension(Dimension dimension) {
    int w = toCanvasDimension(dimension.width);
    int h = toCanvasDimension(dimension.height);
    return new Dimension(w, h);
  }

  private static int toCanvasDimension(int size) {
    return SQUARE_LENGTH * size;
  }
}
