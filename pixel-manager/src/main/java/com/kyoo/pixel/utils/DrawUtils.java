package com.kyoo.pixel.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class DrawUtils {

  public static final double LINE_WIDTH = 2;
  public static final int DOT_SIZE = 2;
  public static final double MAX_HORIZONTAL_LEDS = 800;
  public static final double MAX_VERTICAL_LEDS = 600;

  public static void drawBackground(Graphics2D g2, String hexBackgroundColor, String
      hexDotsColor, Dimension size) {
    // Draw background
    g2.setColor(java.awt.Color.decode(hexBackgroundColor));
    g2.fillRect(0, 0, size.width, size.height);

    // Draw dots
    g2.setColor(java.awt.Color.decode(hexDotsColor));
    for (int i = 0; i < MAX_HORIZONTAL_LEDS; i++) {
      for (int j = 0; j < MAX_VERTICAL_LEDS; j++) {
        Point mousePoint = PositionUtils.toCanvasPosition(i, j);
        g2.fillOval(mousePoint.x + PositionUtils.HALF_SQUARE_LENGTH - 1,
            mousePoint.y + PositionUtils.HALF_SQUARE_LENGTH - 1,
            DOT_SIZE
            , DOT_SIZE);
      }
    }
  }

  public static void drawLed(GraphicsContext gc, String hexColor, Point position) {
    gc.setFill(Color.web(hexColor));
    gc.fillOval(position.x,
        position.y,
        PositionUtils.SQUARE_LENGTH,
        PositionUtils.SQUARE_LENGTH);
  }

  public static void drawMousePointer(GraphicsContext gc, String hexColor, Point position) {
    gc.setLineWidth(LINE_WIDTH);
    gc.setStroke(Color.web(hexColor));
    gc.strokeRect(position.x,
        position.y,
        PositionUtils.SQUARE_LENGTH,
        PositionUtils.SQUARE_LENGTH);
  }

  public static void drawMouseText(GraphicsContext gc, String hexColor,
      Point position, String text) {
    gc.setFill(Color.web(hexColor));
    gc.fillText(text, position.x, position.y);
  }

  public static void selectRect(GraphicsContext gc, String hexColor, Point position,
      Dimension size) {
    gc.setStroke(Color.web(hexColor));
    gc.setLineWidth(1);
    gc.setLineDashes(10);
    gc.strokeRect(position.x, position.y, size.width, size.height);
  }
}
