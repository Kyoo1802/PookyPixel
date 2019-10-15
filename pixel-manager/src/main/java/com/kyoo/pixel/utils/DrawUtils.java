package com.kyoo.pixel.utils;

import static javafx.scene.paint.Color.GRAY;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.YELLOW;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.DriverPort;
import com.kyoo.pixel.connection.components.Led;
import com.kyoo.pixel.connection.components.SquarePanel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawLedPathCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawSquarePanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.MovementCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.ScaleCommandRequest;
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
        g2.fillOval(mousePoint.x + PositionUtils.HALF_SQUARE_LENGTH - DOT_SIZE / 2,
            mousePoint.y + PositionUtils.HALF_SQUARE_LENGTH - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
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
      Point ledPosition = new Point(ledCanvasPosition.x, ledCanvasPosition.y);
      ;
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

  public static void drawSelectText(GraphicsContext gc, String hexColor,
      Point position, String text) {
    gc.setFill(Color.web(hexColor));
    gc.fillText(text, position.x + PositionUtils.SQUARE_LENGTH,
        position.y + PositionUtils.SQUARE_LENGTH);
  }

  public static void selectRect(GraphicsContext gc, String hexColor, Point position,
      Dimension size) {
    gc.setStroke(Color.web(hexColor));
    gc.setLineWidth(1);
    gc.setLineDashes(10);
    gc.strokeRect(position.x, position.y, size.width, size.height);
  }

  public static void selectResize(GraphicsContext gc, String hexColor, Point startPosition,
      Point endPosition) {
    gc.setFill(Color.web(hexColor));
    gc.setLineWidth(1);
    gc.setLineDashes();
    gc.fillRect(startPosition.x, startPosition.y, PositionUtils.HALF_SQUARE_LENGTH,
        PositionUtils.HALF_SQUARE_LENGTH);

    gc.fillRect(startPosition.x,
        endPosition.y + PositionUtils.HALF_SQUARE_LENGTH, PositionUtils.HALF_SQUARE_LENGTH,
        PositionUtils.HALF_SQUARE_LENGTH);

    gc.fillRect(endPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
        startPosition.y, PositionUtils.HALF_SQUARE_LENGTH,
        PositionUtils.HALF_SQUARE_LENGTH);

    gc.fillRect(endPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
        endPosition.y + PositionUtils.HALF_SQUARE_LENGTH, PositionUtils.HALF_SQUARE_LENGTH,
        PositionUtils.HALF_SQUARE_LENGTH);
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

  public static void drawDriverPortPointer(GraphicsContext gc, Point canvasPosition) {
    gc.fillOval(canvasPosition.x, canvasPosition.y, 20, 20);
  }

  public static void drawComponentSelection(GraphicsContext gc, ConnectionProperties properties,
      ConnectionComponent component) {
    Point startPosition = PositionUtils.toCanvasPosition(component.getStartIdxPosition());
    Point endPosition = PositionUtils.toCanvasPosition(component.getEndIdxPosition());
    Dimension size = PositionUtils.toCanvasDimension(component.getSize());

    DrawUtils.selectRect(gc, properties.getSelectColor(), startPosition, size);
    DrawUtils
        .selectResize(gc, properties.getSelectColor(), startPosition, endPosition);
    DrawUtils
        .drawSelectText(gc, properties.getSelectColor(), endPosition,
            component.description());
  }

  public static void drawSquarePanelCommand(GraphicsContext gc, ConnectionProperties properties,
      DrawSquarePanelCommandRequest request, Point pointer) {
    Point mouseCanvasPosition = PositionUtils.toCanvasPosition(pointer);
    Point startPosition = PositionUtils.toCanvasPosition(
        request.getStartIdxPosition().y, request.getStartIdxPosition().x);

    DrawUtils.drawLed(gc, properties.getLedStartColor(), startPosition);

    Dimension size = new Dimension(
        mouseCanvasPosition.x - startPosition.x + PositionUtils.SQUARE_LENGTH,
        mouseCanvasPosition.y - startPosition.y + PositionUtils.SQUARE_LENGTH);
    DrawUtils.selectRect(gc, properties.getSelectColor(), startPosition, size);

    Dimension dimension =
        new Dimension(pointer.x - request.getStartIdxPosition().x + 1,
            pointer.y - request.getStartIdxPosition().y + 1);
    if (dimension.width > 0 && dimension.height > 0) {
      String sizeText = String.format("[%d, %d]", dimension.width, dimension.height);
      DrawUtils.drawSelectText(gc, properties.getSelectColor(), mouseCanvasPosition, sizeText);
    }

  }

  public static void drawLedPathCommand(GraphicsContext gc, ConnectionProperties properties,
      DrawLedPathCommandRequest request, Point pointer) {
    Point minPoint = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    Point maxPoint = new Point(0, 0);

    gc.setStroke(YELLOW);
    gc.setLineWidth(1);
    gc.setLineDashes();
    gc.beginPath();
    int i = 0;
    for (Point p : request.getIdxPositions()) {
      Point startPosition = PositionUtils.toCanvasPosition(p.y, p.x);
      minPoint.x = Math.min(minPoint.x, startPosition.x);
      minPoint.y = Math.min(minPoint.y, startPosition.y);
      maxPoint.x = Math.max(maxPoint.x, startPosition.x);
      maxPoint.y = Math.max(maxPoint.y, startPosition.y);
      gc.setStroke(YELLOW);
      if (i == 0) {
        gc.moveTo(startPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
            startPosition.y + PositionUtils.HALF_SQUARE_LENGTH);
        gc.setFill(GREEN);
        DrawUtils.drawLed(gc, properties.getLedOffColor(), startPosition);
      } else {
        gc.lineTo(startPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
            startPosition.y + PositionUtils.HALF_SQUARE_LENGTH);

        gc.setFill(GRAY);
        DrawUtils.drawLed(gc, properties.getLedOffColor(), startPosition);
      }
      i++;
    }
    gc.stroke();
    Dimension size = new Dimension(
        maxPoint.x - minPoint.x + PositionUtils.SQUARE_LENGTH,
        maxPoint.y - minPoint.y + PositionUtils.SQUARE_LENGTH);
    DrawUtils.selectRect(gc, properties.getSelectColor(), minPoint, size);

    String sizeText = String.format("[%d]", request.getIdxPositions().size());
    DrawUtils.drawSelectText(gc, properties.getSelectColor(), maxPoint, sizeText);
  }

  public static void drawMovementCommand(GraphicsContext gc, ConnectionProperties properties,
      MovementCommandRequest request, Point pointer) {

    Point start = PositionUtils.toCanvasPosition(request.getStartIdxPosition());
    Point end = PositionUtils.toCanvasPosition(pointer);
    gc.setLineWidth(1);
    gc.setStroke(WHITE);
    gc.setLineDashes(10);
    gc.strokeLine(start.x, start.y, end.x, end.y);
  }

  public static void drawScaleCommand(GraphicsContext gc, ConnectionProperties properties,
      ScaleCommandRequest request, Point pointer, Dimension currentSize) {

    Point start = PositionUtils.toCanvasPosition(request.getStartIdxPosition());
    Point end = PositionUtils.toCanvasPosition(pointer);
    gc.setLineWidth(1);
    gc.setStroke(WHITE);
    gc.setLineDashes(10);
    gc.strokeLine(start.x, start.y, end.x + PositionUtils.SQUARE_LENGTH,
        end.y + PositionUtils.SQUARE_LENGTH);

    Dimension newSize =
        new Dimension(currentSize.getSize().width + pointer.x - request.getStartIdxPosition().x,
            currentSize.height + pointer.y - request.getStartIdxPosition().y);

    if (newSize.width > 0 && newSize.height > 0) {
      String sizeText = String
          .format("[%d, %d] = %d", newSize.width, newSize.height, newSize.width * newSize.height);
      DrawUtils.drawSelectText(gc, properties.getSelectColor(), end, sizeText);
    }
  }
}
