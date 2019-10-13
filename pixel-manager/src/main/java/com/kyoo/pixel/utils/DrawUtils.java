package com.kyoo.pixel.utils;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.YELLOW;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.DriverPort;
import com.kyoo.pixel.connection.components.Led;
import com.kyoo.pixel.connection.components.SquarePanel;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class DrawUtils {

  public static final double LINE_WIDTH = 1;
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
            mousePoint.y + PositionUtils.HALF_SQUARE_LENGTH - 1, DOT_SIZE, DOT_SIZE);
      }
    }
  }

  public static void drawSquarePanel(GraphicsContext gc, ConnectionProperties properties,
      SquarePanel sp) {
    Point points[] = new Point[sp.getLeds().size()];
    int idx = 0;
    for (Led led : sp.getLeds().values()) {
      Point ledCanvasPosition = PositionUtils
          .toCanvasPosition(led.getIdxPosition().y, led.getIdxPosition().x);
      String hexColor;
      if (idx == 0) {
        hexColor = properties.getLedStartColor();
      } else if (idx == sp.getLeds().size() - 1) {
        hexColor = properties.getLedEndColor();
      } else {
        hexColor = properties.getLedOffColor();
      }
      Point ledPosition = getLedPanelPosition(ledCanvasPosition);
      points[idx++] = ledPosition;
      drawLed(gc, hexColor, ledPosition);
    }
    gc.setStroke(YELLOW);
    gc.setLineWidth(1);
    gc.setLineDashes();
    gc.beginPath();
    for (int i = 0; i < points.length; i++) {
      if (i == 0) {
        gc.moveTo(points[i].x + PositionUtils.HALF_SQUARE_LENGTH,
            points[i].y + PositionUtils.HALF_SQUARE_LENGTH);
      } else {
        gc.lineTo(points[i].x + PositionUtils.HALF_SQUARE_LENGTH,
            points[i].y + PositionUtils.HALF_SQUARE_LENGTH);
      }

    }
    gc.stroke();
  }

  private static Point getLedPanelPosition(Point ledCanvasPosition) {
    return new Point(ledCanvasPosition.x, ledCanvasPosition.y);
  }

  public static void drawLed(GraphicsContext gc, String hexColor, Point position) {
    gc.setFill(Color.web(hexColor));
    gc.fillOval(position.x, position.y, PositionUtils.SQUARE_LENGTH, PositionUtils.SQUARE_LENGTH);
  }

  public static void drawMousePointer(GraphicsContext gc, String hexColor, Point position) {
    gc.setLineWidth(LINE_WIDTH);
    gc.setStroke(Color.web(hexColor));
    gc.setLineDashes();
    gc.strokeRect(position.x, position.y, PositionUtils.SQUARE_LENGTH + 4,
        PositionUtils.SQUARE_LENGTH + 4);
  }

  public static void drawMouseText(GraphicsContext gc, String hexColor,
      Point position, String text) {
//    gc.setFill(Color.web("#0000ff"));
    gc.setFill(WHITE);
    gc.fillText(text, position.x + PositionUtils.SQUARE_LENGTH,
        position.y + PositionUtils.SQUARE_LENGTH);
  }

  public static void selectRect(GraphicsContext gc, String hexColor, Point position,
      Dimension size) {
    gc.setStroke(Color.web(hexColor));
    gc.setLineWidth(1);
    gc.setLineDashes(10);
    gc.strokeRect(position.x - PositionUtils.HALF_SQUARE_LENGTH / 2,
        position.y - PositionUtils.HALF_SQUARE_LENGTH / 2, size.width, size.height);
  }

  public static void drawPort(GraphicsContext gc, ConnectionProperties properties,
      DriverPort port) {
    Point canvasPosition = PositionUtils
        .toCanvasPosition(port.getIdxPosition().y, port.getIdxPosition().x);
    Dimension canvasDimension = PositionUtils.toCanvasDimension(port.getSize());

    String hexColor = properties.getDriverPortColor();
    gc.setFill(Color.web(hexColor));
    gc.fillOval(canvasPosition.x, canvasPosition.y, canvasDimension.width, canvasDimension.height);
  }

  public static void drawTempPort(GraphicsContext gc, Point canvasPosition) {
    gc.fillOval(canvasPosition.x, canvasPosition.y, 20, 20);
  }
}
