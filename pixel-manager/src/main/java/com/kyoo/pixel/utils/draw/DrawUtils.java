package com.kyoo.pixel.utils.draw;

import com.kyoo.pixel.utils.Cache;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;

public final class DrawUtils {

  public static final int DOT_SIZE = 2;
  public static final double MAX_HORIZONTAL_LEDS = 800;
  public static final double MAX_VERTICAL_LEDS = 600;
  protected static final Cache<String, Image> OBJECTS_CACHE = new Cache<>(2);

  protected static void drawLed(GraphicsContext gc, String hexColor, Point position) {
    gc.setFill(Color.web(hexColor));
    gc.fillOval(position.x, position.y, PositionUtils.SQUARE_LENGTH, PositionUtils.SQUARE_LENGTH);
  }

  protected static void drawText(GraphicsContext gc, String hexColor, Point position, String text) {
    gc.setFill(Color.web(hexColor));
    gc.fillText(
        text, position.x + PositionUtils.SQUARE_LENGTH, position.y + PositionUtils.SQUARE_LENGTH);
  }

  protected static void drawSelectionRect(
      GraphicsContext gc, String hexColor, Point position, Dimension size) {
    gc.setStroke(Color.web(hexColor));
    gc.setLineWidth(1);
    gc.setLineDashes(10);
    gc.strokeRect(position.x, position.y, size.width, size.height);
  }
}
