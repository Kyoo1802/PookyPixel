package com.kyoo.pixel.utils;

import static com.kyoo.pixel.utils.DrawUtils.DOT_SIZE;
import static com.kyoo.pixel.utils.DrawUtils.MAX_HORIZONTAL_LEDS;
import static com.kyoo.pixel.utils.DrawUtils.MAX_VERTICAL_LEDS;
import static com.kyoo.pixel.utils.DrawUtils.drawSelectionRect;
import static com.kyoo.pixel.utils.DrawUtils.drawText;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.LedComponent;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.connection.components.SelectableComponent.SelectedSide;
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

public final class DrawComponentUtils {

  public static final Cache<LedComponent, Image> componentCache = new Cache<>();


  public static void getBackground(GraphicsContext gc, Dimension size,
      ConnectionProperties properties) {
    Image img = DrawUtils.OBJECTS_CACHE.get("Background-" + size, o -> {

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

  public static void drawLedComponent(GraphicsContext gc, ConnectionProperties properties,
      LedComponent ledComponent) {
    Image img = componentCache.get(ledComponent, component -> {
      Dimension ledComponentSize = PositionUtils.toCanvasDimension(component.getSize());
      BufferedImage bufferedImage = new BufferedImage(ledComponentSize.width,
          ledComponentSize.height, BufferedImage.TYPE_4BYTE_ABGR);
      Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

      // Draw Leds
      int[] pathXPoint = new int[component.getLeds().size()];
      int[] pathYPoint = new int[component.getLeds().size()];
      int idx = 0;
      for (Led led : component.getLeds()) {
        String hexColor;
        if (idx == 0) {
          hexColor = properties.getLedStartColor();
        } else if (idx == component.getLeds().size() - 1) {
          hexColor = properties.getLedEndColor();
        } else {
          hexColor = properties.getLedOffColor();
        }
        Point ledCanvasPosition = PositionUtils.toCanvasPosition(led.getStartIdxPosition());
        pathXPoint[idx] = ledCanvasPosition.x + PositionUtils.HALF_SQUARE_LENGTH;
        pathYPoint[idx] = ledCanvasPosition.y + PositionUtils.HALF_SQUARE_LENGTH;

        // Draw led
        g2d.setColor(java.awt.Color.decode(hexColor));
        g2d.fillOval(ledCanvasPosition.x, ledCanvasPosition.y, PositionUtils.SQUARE_LENGTH,
            PositionUtils.SQUARE_LENGTH);
        idx++;
      }

      // Draw Connection Path
      g2d.setColor(java.awt.Color.decode(properties.getLedConnectionPathColor()));
      g2d.drawPolyline(pathXPoint, pathYPoint, pathXPoint.length);
      g2d.dispose();

      return SwingFXUtils.toFXImage(bufferedImage, null);
    });

    // Draw cached Image
    Point ledCanvasPosition = PositionUtils.toCanvasPosition(ledComponent.getStartIdxPosition());
    gc.drawImage(img, ledCanvasPosition.x, ledCanvasPosition.y);
  }

  public static void drawDriverPort(GraphicsContext gc, ConnectionProperties properties,
      DriverPort port) {
    Point canvasPosition = PositionUtils
        .toCanvasPosition(port.getStartIdxPosition().y, port.getStartIdxPosition().x);
    Dimension canvasDimension = PositionUtils.toCanvasDimension(port.getSize());
    gc.setFill(Color.web(properties.getDriverPortColor()));
    gc.fillOval(canvasPosition.x, canvasPosition.y, canvasDimension.width, canvasDimension.height);
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
      drawSelectionRect(gc, properties.getSelectColor(), minPoint, size);

      // Draw component info
      String sizeText = String.format("[%d]", ledPath.size());
      drawText(gc, properties.getSelectColor(), maxPoint, sizeText);
    }
  }

  public static void drawBridge(GraphicsContext gc, ConnectionProperties properties,
      Bridge bridge) {
    Point start = getBridgePosition(bridge.getStartComponent(), true);
    Point end = getBridgePosition(bridge.getEndComponent(), false);
    gc.setLineWidth(2);
    gc.setStroke(Color.web(properties.getBridgeColor()));
    gc.setLineDashes();
    gc.strokeLine(start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH,
        end.x + PositionUtils.HALF_SQUARE_LENGTH, end.y + PositionUtils.HALF_SQUARE_LENGTH);
  }

  protected static Point getBridgePosition(ConnectionComponent c, boolean isStart) {
    if (c instanceof LedComponent) {
      LedComponent lc = ((LedComponent) c);
      Led l = isStart ? lc.getLastLed() : lc.getFirstLed();
      return PositionUtils
          .toCanvasPosition(lc.getStartIdxPosition().y + l.getStartIdxPosition().y,
              lc.getStartIdxPosition().x + l.getStartIdxPosition().x);
    } else {
      return PositionUtils.toCanvasPosition(c.getStartIdxPosition().y, c.getStartIdxPosition().x);
    }
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

  private static void drawResizePoints(GraphicsContext gc, String hexColor, Point startPosition,
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
}
