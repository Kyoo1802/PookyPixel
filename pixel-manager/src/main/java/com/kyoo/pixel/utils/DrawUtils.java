package com.kyoo.pixel.utils;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.LedComponent;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.connection.components.SelectableComponent.SelectedSide;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawBridgeCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawLedPathCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawSquarePanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.MovementCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.ScaleCommandRequest;
import com.kyoo.pixel.connection.components.impl.Bridge;
import com.kyoo.pixel.connection.components.impl.DriverPort;
import com.kyoo.pixel.connection.components.impl.Led;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Optional;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public final class DrawUtils {

  public static final int DOT_SIZE = 2;
  public static final double MAX_HORIZONTAL_LEDS = 800;
  public static final double MAX_VERTICAL_LEDS = 600;
  private static final Cache<LedComponent, Image> componentCache = new Cache<>();
  private static final Cache<String, Image> objectsCache = new Cache<>(2);

  public static void getBackground(GraphicsContext gc, Dimension size,
      ConnectionProperties properties) {
    Image img = objectsCache.get("Background-" + size, o -> {

      Dimension canvasDimension = PositionUtils.toCanvasDimension(size);
      BufferedImage bufferedImage =
          new BufferedImage(canvasDimension.width, canvasDimension.height,
              BufferedImage.TYPE_INT_ARGB);

      Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

      // Draw background
      g2d.setColor(java.awt.Color.decode(properties.getBackgroundColor()));
      g2d.fillRect(0, 0, canvasDimension.width, canvasDimension.height);

      // Draw dots
      g2d.setColor(java.awt.Color.decode(properties.getBackgroundDotsColor()));
      for (int i = 0; i < MAX_HORIZONTAL_LEDS; i++) {
        for (int j = 0; j < MAX_VERTICAL_LEDS; j++) {
          Point mousePoint = PositionUtils.toCanvasPosition(i, j);
          g2d.fillOval(mousePoint.x + PositionUtils.HALF_SQUARE_LENGTH - DOT_SIZE / 2,
              mousePoint.y + PositionUtils.HALF_SQUARE_LENGTH - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
        }
      }
      g2d.dispose();
      return SwingFXUtils.toFXImage(bufferedImage, null);
    });
    // Draw cached Image
    gc.drawImage(img, 0, 0);
  }


  public static void drawDefaultPointer(GraphicsContext gc, ConnectionProperties properties,
      Point position) {
    gc.setLineWidth(properties.getMouseWidth());
    gc.setStroke(Color.web(properties.getMouseColor()));
    gc.setLineDashes();
    gc.strokeRect(position.x, position.y, PositionUtils.SQUARE_LENGTH + 4,
        PositionUtils.SQUARE_LENGTH + 4);
  }

  public static void drawDriverPortPointer(GraphicsContext gc, ConnectionProperties properties,
      Point canvasPosition) {
    gc.setLineWidth(properties.getMouseWidth());
    gc.setStroke(Color.web(properties.getMouseColor()));
    gc.fillOval(canvasPosition.x, canvasPosition.y, 20, 20);
  }

  public static void drawLedComponent(GraphicsContext gc, ConnectionProperties properties,
      LedComponent ledComponent) {
    Image img = componentCache.get(ledComponent, lc -> {
      Dimension ledComponentSize = PositionUtils.toCanvasDimension(lc.getSize());
      BufferedImage bufferedImage = new BufferedImage(ledComponentSize.width,
          ledComponentSize.height, BufferedImage.TYPE_4BYTE_ABGR);
      Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

      // Draw Leds
      int[] xPoint = new int[lc.getLeds().size()];
      int[] yPoint = new int[lc.getLeds().size()];
      int idx = 0;
      for (Led led : lc.getLeds()) {
        Point ledCanvasPosition = PositionUtils
            .toCanvasPosition(led.getStartIdxPosition().y - lc.getStartIdxPosition().y,
                led.getStartIdxPosition().x - lc.getStartIdxPosition().x);
        String hexColor;
        if (idx == 0) {
          hexColor = properties.getLedStartColor();
        } else if (idx == lc.getLeds().size() - 1) {
          hexColor = properties.getLedEndColor();
        } else {
          hexColor = properties.getLedOffColor();
        }
        Point ledPosition = new Point(ledCanvasPosition.x - PositionUtils.HALF_SQUARE_LENGTH,
            ledCanvasPosition.y - PositionUtils.HALF_SQUARE_LENGTH);

        xPoint[idx] = ledPosition.x + PositionUtils.HALF_SQUARE_LENGTH;
        yPoint[idx] = ledPosition.y + PositionUtils.HALF_SQUARE_LENGTH;

        // Draw led
        g2d.setColor(java.awt.Color.decode(hexColor));
        g2d.fillOval(ledPosition.x, ledPosition.y, PositionUtils.SQUARE_LENGTH,
            PositionUtils.SQUARE_LENGTH);
        idx++;
      }

      // Draw Connection Path
      g2d.setColor(java.awt.Color.decode(properties.getLedConnectionPathColor()));
      g2d.drawPolyline(xPoint, yPoint, xPoint.length);
      g2d.dispose();

      return SwingFXUtils.toFXImage(bufferedImage, null);
    });

    // Draw cached Image
    Point ledCanvasPosition = PositionUtils.toCanvasPosition(ledComponent.getStartIdxPosition());
    gc.drawImage(img, ledCanvasPosition.x, ledCanvasPosition.y);
  }

  public static void drawPort(GraphicsContext gc, ConnectionProperties properties,
      DriverPort port) {
    Point canvasPosition = PositionUtils
        .toCanvasPosition(port.getStartIdxPosition().y, port.getStartIdxPosition().x);
    Dimension canvasDimension = PositionUtils.toCanvasDimension(port.getSize());
    gc.setFill(Color.web(properties.getDriverPortColor()));
    gc.fillOval(canvasPosition.x, canvasPosition.y, canvasDimension.width, canvasDimension.height);
  }

  public static void drawComponentSelection(GraphicsContext gc, ConnectionProperties properties,
      SelectableComponent component) {
    if (component.getSelectedSide() == SelectedSide.NONE) {
      return;
    }
    Point startPosition = PositionUtils.toCanvasPosition(component.getStartIdxPosition());
    Point endPosition = PositionUtils.toCanvasPosition(component.getEndIdxPosition());
    Dimension size = PositionUtils.toCanvasDimension(component.getSize());

    drawSelectionRect(gc, properties.getSelectColor(), startPosition, size);
    drawResizePoints(gc, properties.getSelectColor(), startPosition, endPosition);
    drawText(gc, properties.getSelectColor(), endPosition,
        component.description());
  }

  public static void drawResizePoints(GraphicsContext gc, String hexColor, Point startPosition,
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

  public static void drawSquarePanelCommand(GraphicsContext gc, ConnectionProperties properties,
      DrawSquarePanelCommandRequest request, Point pointer) {
    Point startPosition = PositionUtils.toCanvasPosition(
        request.getStartIdxPosition().y, request.getStartIdxPosition().x);

    // Draw first led
    drawLed(gc, properties.getLedStartColor(), startPosition);

    // Draw component preview
    Point mouseCanvasPosition = PositionUtils.toCanvasPosition(pointer);
    Dimension size = new Dimension(
        mouseCanvasPosition.x - startPosition.x + PositionUtils.SQUARE_LENGTH,
        mouseCanvasPosition.y - startPosition.y + PositionUtils.SQUARE_LENGTH);
    drawSelectionRect(gc, properties.getSelectColor(), startPosition, size);

    // Draw Text info
    Dimension dimension =
        new Dimension(pointer.x - request.getStartIdxPosition().x + 1,
            pointer.y - request.getStartIdxPosition().y + 1);
    if (dimension.width > 0 && dimension.height > 0) {
      String sizeText = String.format("[%d, %d]", dimension.width, dimension.height);
      DrawUtils.drawText(gc, properties.getSelectColor(), mouseCanvasPosition, sizeText);
    }
  }

  public static void drawLedPathCommand(GraphicsContext gc, ConnectionProperties properties,
      DrawLedPathCommandRequest request, Point pointer) {
    drawLedPath(gc, properties, request.getIdxPositions(), Optional.of(pointer));
  }

  public static void drawBridgeCommand(GraphicsContext gc, ConnectionProperties properties,
      DrawBridgeCommandRequest request, Point pointer) {
    Point start = request.getStartComponent() instanceof LedComponent ?
        ((LedComponent) request.getStartComponent()).getLastLed().getStartIdxPosition()
        : request.getStartComponent().getStartIdxPosition();
    start = PositionUtils.toCanvasPosition(start);

    Point end = PositionUtils.toCanvasPosition(pointer);
    gc.setLineWidth(2);
    gc.setStroke(Color.web(properties.getBridgeColor()));
    gc.setLineDashes();
    gc.strokeLine(start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH,
        end.x + PositionUtils.HALF_SQUARE_LENGTH, end.y + PositionUtils.HALF_SQUARE_LENGTH);
  }

  public static void drawMovementCommand(GraphicsContext gc, ConnectionProperties properties,
      MovementCommandRequest request, Point pointer) {
    Point start = PositionUtils.toCanvasPosition(request.getStartIdxPosition());
    Point end = PositionUtils.toCanvasPosition(pointer);
    gc.setLineWidth(1);
    gc.setStroke(Color.web(properties.getSelectColor()));
    gc.setLineDashes(10);
    gc.strokeLine(start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH,
        end.x + PositionUtils.HALF_SQUARE_LENGTH, end.y + PositionUtils.HALF_SQUARE_LENGTH);
  }

  public static void drawScaleCommand(GraphicsContext gc, ConnectionProperties properties,
      ScaleCommandRequest request, Point idxPointer) {

    // Draw Scale square
    Point start = PositionUtils.toCanvasPosition(request.getComponentStartIdxPoint());
    Dimension dimension =
        PositionUtils
            .toCanvasDimension(new Dimension(idxPointer.x - request.getComponentStartIdxPoint().x,
                idxPointer.y - request.getComponentStartIdxPoint().x));
    Point end = PositionUtils.toCanvasPosition(idxPointer);

    gc.setLineWidth(properties.getSelectWidth());
    gc.setStroke(Color.web(properties.getSelectColor()));
    gc.setLineDashes(10);
    gc.strokeRect(start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH, dimension.width, dimension.height);

    // Draw scale info
    if (dimension.width > 0 && dimension.height > 0) {
      String sizeText = String
          .format("[%d, %d] = %d", dimension.width, dimension.height,
              dimension.width * dimension.height);
      DrawUtils.drawText(gc, properties.getSelectColor(), end, sizeText);
    }
  }

  public static void drawLedPath(GraphicsContext gc, ConnectionProperties properties,
      Collection<Led> ledPath, Optional<Point> pointer) {
    Point minPoint = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    Point maxPoint = new Point(0, 0);

    gc.setLineWidth(1);
    gc.setLineDashes();
    gc.beginPath();
    int i = 0;

    // Draw Leds
    for (Led led : ledPath) {
      Point p = led.getStartIdxPosition();
      Point startPosition = PositionUtils.toCanvasPosition(p.y, p.x);
      minPoint.x = Math.min(minPoint.x, startPosition.x);
      minPoint.y = Math.min(minPoint.y, startPosition.y);
      maxPoint.x = Math.max(maxPoint.x, startPosition.x);
      maxPoint.y = Math.max(maxPoint.y, startPosition.y);
      if (i == 0) {
        gc.moveTo(startPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
            startPosition.y + PositionUtils.HALF_SQUARE_LENGTH);
        DrawUtils.drawLed(gc, properties.getLedStartColor(), startPosition);
      } else {
        gc.lineTo(startPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
            startPosition.y + PositionUtils.HALF_SQUARE_LENGTH);
        DrawUtils.drawLed(gc, properties.getLedOffColor(), startPosition);
      }
      i++;
    }
    if (pointer.isPresent()) {
      Point canvasPosition = PositionUtils.toCanvasPosition(pointer.get());
      gc.lineTo(canvasPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
          canvasPosition.y + PositionUtils.HALF_SQUARE_LENGTH);
    }
    gc.setStroke(Color.web(properties.getLedConnectionPathColor()));
    gc.stroke();

    if (pointer.isPresent()) {
      // Draw component preview
      Dimension size = new Dimension(
          maxPoint.x - minPoint.x + PositionUtils.SQUARE_LENGTH,
          maxPoint.y - minPoint.y + PositionUtils.SQUARE_LENGTH);
      DrawUtils.drawSelectionRect(gc, properties.getSelectColor(), minPoint, size);

      // Draw component info
      String sizeText = String.format("[%d]", ledPath.size());
      DrawUtils.drawText(gc, properties.getSelectColor(), maxPoint, sizeText);
    }
  }

  public static void drawLed(GraphicsContext gc, String hexColor, Point position) {
    gc.setFill(Color.web(hexColor));
    gc.fillOval(position.x, position.y, PositionUtils.SQUARE_LENGTH, PositionUtils.SQUARE_LENGTH);
  }

  public static void drawText(GraphicsContext gc, String hexColor, Point position, String text) {
    gc.setFill(Color.web(hexColor));
    gc.fillText(text, position.x + PositionUtils.SQUARE_LENGTH,
        position.y + PositionUtils.SQUARE_LENGTH);
  }

  public static void drawSelectionRect(GraphicsContext gc, String hexColor, Point position,
      Dimension size) {
    gc.setStroke(Color.web(hexColor));
    gc.setLineWidth(1);
    gc.setLineDashes(10);
    gc.strokeRect(position.x, position.y, size.width, size.height);
  }

  public static void drawBridge(GraphicsContext gc, ConnectionProperties properties,
      Bridge component) {
    Point start = PositionUtils.toCanvasPosition(component.getStartIdxPosition());
    Point end = PositionUtils.toCanvasPosition(component.getEndIdxPosition());
    gc.setLineWidth(2);
    gc.setStroke(Color.web(properties.getBridgeColor()));
    gc.setLineDashes();
    gc.strokeLine(start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH,
        end.x + PositionUtils.HALF_SQUARE_LENGTH, end.y + PositionUtils.HALF_SQUARE_LENGTH);
  }
}
