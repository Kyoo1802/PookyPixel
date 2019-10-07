package com.kyoo.pixel.views.connection;

import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.YELLOW;

import com.kyoo.pixel.data.connection.ConnectionComponent;
import com.kyoo.pixel.data.connection.SquarePanel;
import com.kyoo.pixel.views.connection.ConnectionModel.ConnectionAction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ConnectionCanvasRenderer {

  public static final int CIRCLE_DIAMETER = 10;
  public static final int DOT_SIZE = 2;
  public static final int MARGIN = CIRCLE_DIAMETER;
  public static final double LINE_WIDTH = 2;
  public static final double MAX_WIDTH = 800;
  public static final double MAX_HEIGHT = 600;

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

  private void createdComponents(GraphicsContext gc) {
    Point mouseSquare = getStartSquarePoint(connectionViewModel.positionProperty().get());
    if(connectionViewModel.getConnectionModel().getCreatedComponents().all().isEmpty()){
      return;
    }
    for(ConnectionComponent cc :
        connectionViewModel.getConnectionModel().getCreatedComponents().all()){
      switch (cc.connectionType()){
        case SQUARE_PANEL:
          SquarePanel sp = (SquarePanel)cc;

          gc.setStroke(WHITE);
          gc.setLineWidth(1);
          gc.setLineDashes(10);
          gc.strokeRect(sp.getStartPoint().x, sp.getStartPoint().y,
              mouseSquare.x- sp.getStartPoint().x, mouseSquare.y-sp.getStartPoint().y);
        default:
      }
    }
  }

  private void currentComponent(GraphicsContext gc) {
    Point mouseSquare = getStartSquarePoint(connectionViewModel.positionProperty().get());
    if(connectionViewModel.getConnectionModel().getBeingCreatedComponent().isEmpty()){
      return;
    }
    ConnectionComponent cc =
        connectionViewModel.getConnectionModel().getBeingCreatedComponent().get();
    switch (cc.connectionType()){
      case SQUARE_PANEL:
        SquarePanel sp = (SquarePanel)cc;
        gc.setStroke(WHITE);
        gc.setLineWidth(1);
        gc.setLineDashes(10);
        gc.strokeRect(sp.getStartPoint().x, sp.getStartPoint().y,
            mouseSquare.x- sp.getStartPoint().x, mouseSquare.y-sp.getStartPoint().y);
      default:
    }
  }

  private void drawMouse(GraphicsContext gc) {
    Point mouseSquare = getStartSquarePoint(connectionViewModel.positionProperty().get());
    switch (connectionViewModel.getConnectionModel().getConnectionAction()){
      case NO_ACTION:
        gc.setLineWidth(LINE_WIDTH);
        gc.setStroke(YELLOW);
        gc.strokeRect(mouseSquare.x,
            mouseSquare.y,
            CIRCLE_DIAMETER,
            CIRCLE_DIAMETER);
        break;
      case DRAW:
        switch (connectionViewModel.getConnectionModel().getDrawAction()) {
          case DRAW_SQUARE_PANEL:
            if(connectionViewModel.getConnectionModel().getBeingCreatedComponent().isEmpty()) {
              gc.setFill(GREEN);
              gc.fillOval(mouseSquare.x,
                  mouseSquare.y,
                  CIRCLE_DIAMETER,
                  CIRCLE_DIAMETER);
            } else {
              gc.setFill(RED);
              gc.fillOval(mouseSquare.x,
                  mouseSquare.y,
                  CIRCLE_DIAMETER,
                  CIRCLE_DIAMETER);
            }
            break;
          default:
        }
    }
  }

  private static Point getStartSquarePoint(Point mousePosition){
    int mouseX2 = mousePosition.x - CIRCLE_DIAMETER / 2;
    int mouseY2 = mousePosition.y - CIRCLE_DIAMETER / 2;
    return new Point(mouseX2 - mouseX2 % CIRCLE_DIAMETER + CIRCLE_DIAMETER / 2,
        mouseY2 - mouseY2 % CIRCLE_DIAMETER + CIRCLE_DIAMETER / 2);
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
    for (int i = 0; i < MAX_WIDTH; i++) {
      for (int j = 0; j < MAX_HEIGHT; j++) {
        graphics2D.fillOval(CIRCLE_DIAMETER * i + MARGIN
                - DOT_SIZE / 2,
            CIRCLE_DIAMETER * j + MARGIN
                - DOT_SIZE / 2,
            DOT_SIZE
            , DOT_SIZE);
      }
    }
    graphics2D.dispose();
    background = bufferedImage;
  }
}
