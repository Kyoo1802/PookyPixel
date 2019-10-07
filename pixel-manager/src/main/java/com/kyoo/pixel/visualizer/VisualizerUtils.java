package com.kyoo.pixel.visualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javafx.scene.canvas.GraphicsContext;

public class VisualizerUtils {

  public static final int CIRCLE_DIAMETER = 10;
  public static final int DOT_SIZE = 2;
  public static final int MARGIN = CIRCLE_DIAMETER;
  public static final double LINE_WIDTH = 2;

  public static void drawSquareMouse(GraphicsContext gc, Point mousePosition) {
    double mouseX2 = mousePosition.x - (CIRCLE_DIAMETER / 2);
    double mouseY2 = mousePosition.y - (CIRCLE_DIAMETER / 2);
    gc.strokeRect(mouseX2 - mouseX2 % CIRCLE_DIAMETER + (CIRCLE_DIAMETER / 2),
        mouseY2 - mouseY2 % CIRCLE_DIAMETER + (CIRCLE_DIAMETER / 2),
        CIRCLE_DIAMETER,
        CIRCLE_DIAMETER);
  }

  public void drawBackground(Dimension dimension) {

    BufferedImage bufferedImage = new BufferedImage((int) dimension.getWidth(),
        (int) dimension.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
    graphics2D.setColor(Color.RED);
    for (int i = 0; i < 800; i++) {
      for (int j = 0; j < 600; j++) {
        graphics2D.fillOval(CIRCLE_DIAMETER * i + MARGIN
                - DOT_SIZE / 2,
            CIRCLE_DIAMETER * j + MARGIN
                - DOT_SIZE / 2,
            DOT_SIZE
            , DOT_SIZE);
      }
    }
    graphics2D.dispose();
//      canvas.getGraphicsContext2D().drawImage(
//          SwingFXUtils.toFXImage(bufferedImage, null), 0, 0);
  }
}
