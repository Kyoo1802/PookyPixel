package com.kyoo.pixel.connection;

import static javafx.scene.paint.Color.GRAY;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.YELLOW;

import com.google.inject.Inject;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.DriverPort;
import com.kyoo.pixel.connection.components.SquarePanel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawLedPathCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawSquarePanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.MovementCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.ScaleCommandRequest;
import com.kyoo.pixel.utils.DrawUtils;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Optional;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ConnectionCanvasRenderer {

  private ConnectionViewModel viewModel;
  private ConnectionModel model;
  private BufferedImage background;
  private ConnectionProperties properties;

  @Inject
  public ConnectionCanvasRenderer(ConnectionViewModel viewModel,
      ConnectionProperties properties) {
    this.viewModel = viewModel;
    this.model = viewModel.getModel();
    this.properties = properties;
  }

  public void render(Canvas connectionCanvas) {
    GraphicsContext gc = connectionCanvas.getGraphicsContext2D();
    drawBackground(gc);
    createdComponents(gc);
    currentComponent(gc);
    drawMousePointer(gc);
  }

  private void drawBackground(GraphicsContext gc) {
    Dimension canvasDimension = PositionUtils.toCanvasDimension(model.getDimension());
    if (background != null && canvasDimension.width == background.getWidth()
        && canvasDimension.getHeight() == background.getHeight()) {
      gc.drawImage(SwingFXUtils.toFXImage(background, null), 0, 0);
      return;
    }
    recreateBackground();
  }

  private void recreateBackground() {
    if (model.getDimension().width * model.getDimension().height == 0) {
      return;
    }

    Dimension canvasDimension = PositionUtils.toCanvasDimension(model.getDimension());
    BufferedImage bufferedImage =
        new BufferedImage(canvasDimension.width, canvasDimension.height,
            BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
    DrawUtils.drawBackground(graphics2D, properties.getBackgroundColor(),
        properties.getBackgroundDotsColor(), canvasDimension);
    graphics2D.dispose();

    background = bufferedImage;
  }

  private void createdComponents(GraphicsContext gc) {
    if (model.getCreatedComponentsManager().all().isEmpty()) {
      return;
    }
    Optional<ConnectionComponent> selectedComponent =
        model.getSelectedComponent();
    for (Map<Long, ConnectionComponent> components :
        model.getCreatedComponentsManager().all().values()) {
      for (ConnectionComponent component : components.values()) {
        // Draw Component
        switch (component.getComponentType()) {
          case SQUARE_PANEL:
            DrawUtils.drawSquarePanel(gc, properties, (SquarePanel) component);
            break;
          case DRIVER_PORT:
            DrawUtils.drawPort(gc, properties, (DriverPort) component);
            break;
          default:
            log.error("Invalid component to draw: " + component.getComponentType());
        }

        // Draw Selection
        if (selectedComponent.isPresent() && selectedComponent.get() == component) {
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
      }
    }
  }

  private void currentComponent(GraphicsContext gc) {
    if (model.getBeingCreatedComponent().isEmpty()) {
      return;
    }

    ConnectionCommandRequest beingCreatedComponent =
        model.getBeingCreatedComponent().get();
    Point mouseCanvasPosition = PositionUtils
        .toCanvasPosition(model.getIdxPointer().getPosition());
    Point mouseIdxPosition =
        PositionUtils.toIdxPosition(new Point(mouseCanvasPosition.x, mouseCanvasPosition.y));

    switch (beingCreatedComponent.getCommandType()) {
      case SQUARE_PANEL: {
        DrawSquarePanelCommandRequest request = (DrawSquarePanelCommandRequest) beingCreatedComponent;

        Point startPosition = PositionUtils.toCanvasPosition(
            request.getStartIdxPosition().y, request.getStartIdxPosition().x);

        DrawUtils.drawLed(gc, properties.getLedStartColor(), startPosition);

        Dimension size = new Dimension(
            mouseCanvasPosition.x - startPosition.x + PositionUtils.SQUARE_LENGTH,
            mouseCanvasPosition.y - startPosition.y + PositionUtils.SQUARE_LENGTH);
        DrawUtils.selectRect(gc, properties.getSelectColor(), startPosition, size);

        Dimension dimension =
            new Dimension(mouseIdxPosition.x - request.getStartIdxPosition().x + 1,
                mouseIdxPosition.y - request.getStartIdxPosition().y + 1);
        if (dimension.width > 0 && dimension.height > 0) {
          String sizeText = String.format("[%d, %d]", dimension.width, dimension.height);
          DrawUtils.drawSelectText(gc, properties.getSelectColor(), mouseCanvasPosition, sizeText);
        }
      }
      break;
      case LED_PATH: {
        DrawLedPathCommandRequest request = (DrawLedPathCommandRequest) beingCreatedComponent;

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
        DrawUtils.drawSelectText(gc, properties.getSelectColor(), mouseCanvasPosition, sizeText);

      }
      break;
      case MOVEMENT: {
        MovementCommandRequest request =
            (MovementCommandRequest) beingCreatedComponent;

        Point start = PositionUtils.toCanvasPosition(request.getStartIdxPosition());
        Point end = PositionUtils.toCanvasPosition(model.getIdxPointer().getPosition());
        gc.setLineWidth(1);
        gc.setStroke(WHITE);
        gc.setLineDashes(10);
        gc.strokeLine(start.x, start.y, end.x, end.y);
      }
      break;
      case SCALE: {
        log.debug("Scale Component");
        ScaleCommandRequest request =
            (ScaleCommandRequest) beingCreatedComponent;

        Point start = PositionUtils.toCanvasPosition(request.getStartIdxPosition());
        Point end = PositionUtils.toCanvasPosition(model.getIdxPointer().getPosition());
        gc.setLineWidth(1);
        gc.setStroke(WHITE);
        gc.setLineDashes(10);
        gc.strokeLine(start.x, start.y, end.x + PositionUtils.SQUARE_LENGTH,
            end.y + PositionUtils.SQUARE_LENGTH);

        ConnectionComponent selectedComponent =
            model.getCreatedComponentsManager().getComponent(request.getTypeToScale(),
                request.getIdToScale()).get();
        Dimension newDimension =
            new Dimension(selectedComponent.getSize().width + model.getIdxPointer().getPosition().x
                - request.getStartIdxPosition().x,
                selectedComponent.getSize().height + model.getIdxPointer().getPosition().y
                    - request.getStartIdxPosition().y);

        if (newDimension.width > 0 && newDimension.height > 0) {
          String sizeText = String.format("[%d, %d] = %d", newDimension.width, newDimension.height,
              newDimension.width * newDimension.height);
          DrawUtils.drawSelectText(gc, properties.getSelectColor(), mouseCanvasPosition, sizeText);
        }
      }
      break;
      default:
        log.error(
            "Invalid current component being created: " + beingCreatedComponent.getCommandType());
    }
  }

  private void drawMousePointer(GraphicsContext gc) {
    Point mouseSquare = PositionUtils
        .toCanvasPosition(model.getIdxPointer().getPosition());

    switch (model.getConnectionState()) {
      case NO_ACTION:
        DrawUtils.drawMousePointer(gc, properties.getNoActionColor(), mouseSquare);
        break;
      case DRAW_LED_PATH:
      case DRAW_SQUARE_PANEL:
        if (model.getBeingCreatedComponent().isEmpty()) {
          DrawUtils.drawLed(gc, properties.getLedStartColor(), mouseSquare);
        } else {
          DrawUtils.drawLed(gc, properties.getLedEndColor(), mouseSquare);
        }
        break;
      case DRAW_DRIVER_PORT:
        DrawUtils.drawTempPort(gc, mouseSquare);
        break;
      default:
        log.error("Invalid Mouse Pointer (Action): " + model.getConnectionState());
    }
  }
}
