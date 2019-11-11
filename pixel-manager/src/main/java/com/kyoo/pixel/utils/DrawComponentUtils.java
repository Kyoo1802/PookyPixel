package com.kyoo.pixel.utils;

import static com.kyoo.pixel.utils.DrawUtils.DOT_SIZE;
import static com.kyoo.pixel.utils.DrawUtils.MAX_HORIZONTAL_LEDS;
import static com.kyoo.pixel.utils.DrawUtils.MAX_VERTICAL_LEDS;
import static com.kyoo.pixel.utils.DrawUtils.drawSelectionRect;

import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.fixtures.components.Component;
import com.kyoo.pixel.fixtures.components.ComponentUnit;
import com.kyoo.pixel.fixtures.components.LedComponent;
import com.kyoo.pixel.fixtures.components.led.Bridge;
import com.kyoo.pixel.fixtures.components.led.Led;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public final class DrawComponentUtils {

  public static final Cache<LedComponent, Image> componentCache = new Cache<>();


  public static void getBackground(GraphicsContext gc, Dimension size,
      FixtureProperties properties) {
    // Draw cached Image
    gc.drawImage(loadBackground(size, properties), 0, 0);
  }

  public static Image loadBackground(Dimension size, FixtureProperties properties) {
    return DrawUtils.OBJECTS_CACHE.get("Background-" + size, o -> {

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
  }

  public static void drawLedComponent(GraphicsContext gc, FixtureProperties properties,
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
      for (Led led : component.getLeds().values()) {
        String hexColor;
        if (idx == 0) {
          hexColor = properties.getLedStartColor();
        } else if (idx == component.getLeds().size() - 1) {
          hexColor = properties.getLedEndColor();
        } else {
          hexColor = properties.getLedOffColor();
        }
        Point ledCanvasPosition = PositionUtils.toCanvasPosition(led.getPosition());
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
    Point ledCanvasPosition = PositionUtils.toCanvasPosition(ledComponent.getPosition());
    gc.drawImage(img, ledCanvasPosition.x, ledCanvasPosition.y);
  }

  public static void drawBridge(GraphicsContext gc, FixtureProperties properties,
      Bridge bridge) {
    Point start = null;//getBridgePosition(bridge.getStartComponentKey(), true);
    Point end = null; //getBridgePosition(bridge.getEndComponentKey(), false);
    gc.setLineWidth(2);
    gc.setStroke(Color.web(properties.getBridgeColor()));
    gc.setLineDashes();
    gc.strokeLine(start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH,
        end.x + PositionUtils.HALF_SQUARE_LENGTH, end.y + PositionUtils.HALF_SQUARE_LENGTH);
  }

  protected static Point getBridgePosition(Component c, boolean isStart) {
    if (c instanceof LedComponent) {
      LedComponent lc = ((LedComponent) c);
      Led l = isStart ? lc.getLastLed() : lc.getFirstLed();
      return PositionUtils
          .toCanvasPosition(lc.getPosition().y + l.getPosition().y,
              lc.getPosition().x + l.getPosition().x);
    } else {
      return PositionUtils.toCanvasPosition(c.getPosition().y, c.getPosition().x);
    }
  }

  public static void drawComponentSelection(GraphicsContext gc, FixtureProperties properties,
      ComponentUnit componentUnit) {
    Point startPosition = PositionUtils.toCanvasPosition(componentUnit.getPosition());
    Point endPosition = PositionUtils.toCanvasPosition(componentUnit.getEndPosition());
    Dimension size = PositionUtils.toCanvasDimension(componentUnit.getSize());

    drawSelectionRect(gc, properties.getSelectColor(), startPosition, size);
    drawResizePoints(gc, properties.getSelectColor(), startPosition, endPosition);
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
