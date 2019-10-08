package com.kyoo.pixel.views.connection;

import static javafx.scene.paint.Color.GRAY;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.YELLOW;

import com.kyoo.pixel.data.connection.ConnectionComponent;
import com.kyoo.pixel.data.connection.Led;
import com.kyoo.pixel.data.connection.SquarePanel;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ConnectionCanvasRenderer {

  public static final int DOT_SIZE = 2;
  public static final double LINE_WIDTH = 2;
  public static final double MAX_HORIZONTAL_LEDS = 800;
  public static final double MAX_VERTICAL_LEDS = 600;

  private ConnectionViewModel connectionViewModel;
  private BufferedImage background;

  public ConnectionCanvasRenderer(ConnectionViewModel connectionViewModel) {
    this.connectionViewModel = connectionViewModel;
    this.connectionViewModel.canvasWidthProperty().addListener(v -> recreateBackground());
    this.connectionViewModel.canvasHeightProperty().addListener(v -> recreateBackground());
  }

  public void render(Canvas connectionCanvas) {
    GraphicsContext gc = connectionCanvas.getGraphicsContext2D();
    drawBackground(gc);
    createdComponents(gc);
    currentComponent(gc);
    drawMouse(gc);
  }

  private void drawBackground(GraphicsContext gc) {
    if (background != null) {
      gc.drawImage(
          SwingFXUtils.toFXImage(background, null), 0, 0);
      return;
    }
    recreateBackground();
  }

  private void recreateBackground() {
    Dimension canvasWidth = new Dimension(connectionViewModel.canvasWidthProperty().get(),
        connectionViewModel.canvasHeightProperty().get());
    if (canvasWidth.width * canvasWidth.height == 0) {
      return;
    }
    BufferedImage bufferedImage =
        new BufferedImage(canvasWidth.width, canvasWidth.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
    graphics2D.setColor(Color.RED);
    for (int i = 0; i < MAX_HORIZONTAL_LEDS; i++) {
      for (int j = 0; j < MAX_VERTICAL_LEDS; j++) {
        Point mousePoint = PositionUtils.toCanvasStartPosition(i, j);
        graphics2D.fillOval(mousePoint.x + PositionUtils.HALF_SQUARE_LENGTH - 1,
            mousePoint.y + PositionUtils.HALF_SQUARE_LENGTH - 1,
            DOT_SIZE
            , DOT_SIZE);
      }
    }
    graphics2D.dispose();
    background = bufferedImage;
  }

  private void createdComponents(GraphicsContext gc) {
    if (connectionViewModel.getConnectionModel().getCreatedComponents().all().isEmpty()) {
      return;
    }
    gc.setFill(GRAY);
    for (ConnectionComponent cc :
        connectionViewModel.getConnectionModel().getCreatedComponents().all()) {
      switch (cc.connectionType()) {
        case SQUARE_PANEL:
          SquarePanel sp = (SquarePanel) cc;
          int idx = 0;
          for (Led led : sp.getLeds().values()) {
            Point canvasStartPosition = PositionUtils
                .toCanvasStartPosition(led.getPosition().y, led.getPosition().x);
            if (idx == 0) {
              gc.setFill(GREEN);
            } else if (idx == sp.getLeds().size() - 1) {
              gc.setFill(RED);
            } else {
              gc.setFill(GRAY);
            }
            gc.fillOval(canvasStartPosition.x, canvasStartPosition.y, PositionUtils.SQUARE_LENGTH
                , PositionUtils.SQUARE_LENGTH);
            idx++;
          }
        default:
      }
    }
  }

  private void currentComponent(GraphicsContext gc) {
    Point mouseSquare = PositionUtils
        .toCanvasStartPosition(connectionViewModel.positionProperty().get());

    if (connectionViewModel.getConnectionModel().getBeingCreatedComponent().isEmpty()) {
      return;
    }
    ConnectionComponent cc =
        connectionViewModel.getConnectionModel().getBeingCreatedComponent().get();
    switch (cc.connectionType()) {
      case SQUARE_PANEL:
        SquarePanel sp = (SquarePanel) cc;
        Point startPosition = PositionUtils.toCanvasStartPosition(
            sp.getStartPosition().y, sp.getStartPosition().x);
        gc.setFill(GREEN);
        gc.fillOval(startPosition.x,
            startPosition.y,
            PositionUtils.SQUARE_LENGTH,
            PositionUtils.SQUARE_LENGTH);
        gc.setStroke(WHITE);
        gc.setLineWidth(1);
        gc.setLineDashes(10);
        gc.strokeRect(startPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
            startPosition.y + PositionUtils.HALF_SQUARE_LENGTH,
            mouseSquare.x - startPosition.x, mouseSquare.y - startPosition.y);
      default:
    }
  }

  private void drawMouse(GraphicsContext gc) {
    Point mouseSquare = PositionUtils
        .toCanvasStartPosition(connectionViewModel.positionProperty().get());

    switch (connectionViewModel.getConnectionModel().getConnectionAction()) {
      case NO_ACTION:
        gc.setLineWidth(LINE_WIDTH);
        gc.setStroke(YELLOW);
        gc.strokeRect(mouseSquare.x,
            mouseSquare.y,
            PositionUtils.SQUARE_LENGTH,
            PositionUtils.SQUARE_LENGTH);
        break;
      case DRAW:
        switch (connectionViewModel.getConnectionModel().getDrawAction()) {
          case DRAW_SQUARE_PANEL:
            if (connectionViewModel.getConnectionModel().getBeingCreatedComponent().isEmpty()) {
              gc.setFill(GREEN);
              gc.fillOval(mouseSquare.x,
                  mouseSquare.y,
                  PositionUtils.SQUARE_LENGTH,
                  PositionUtils.SQUARE_LENGTH);
            } else {
              gc.setFill(RED);
              gc.fillOval(mouseSquare.x,
                  mouseSquare.y,
                  PositionUtils.SQUARE_LENGTH,
                  PositionUtils.SQUARE_LENGTH);
            }
            break;
          default:
        }
    }
  }
}
