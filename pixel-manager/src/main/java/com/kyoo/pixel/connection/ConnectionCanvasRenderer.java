package com.kyoo.pixel.connection;

import static javafx.scene.paint.Color.WHITE;

import com.google.inject.Inject;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.DriverPort;
import com.kyoo.pixel.connection.components.SquarePanel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest;
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
    this.viewModel.getCanvasWidth().addListener(v -> recreateBackground());
    this.viewModel.getCanvasHeight().addListener(v -> recreateBackground());
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
    if (background != null) {
      gc.drawImage(SwingFXUtils.toFXImage(background, null), 0, 0);
      return;
    }
    recreateBackground();
  }

  private void recreateBackground() {
    Dimension canvasDimension = new Dimension(viewModel.getCanvasWidth().get(),
        viewModel.getCanvasHeight().get());
    if (canvasDimension.width * canvasDimension.height == 0) {
      return;
    }

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
            log.error("Invalid component to draw: %s", component.getComponentType());
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
        .toRoundPosition(viewModel.getMousePosition().get());
    Point mouseIdxPosition =
        PositionUtils.toIdxPosition(new Point(mouseCanvasPosition.x, mouseCanvasPosition.y));

    switch (beingCreatedComponent.getCommandType()) {
      case SQUARE_PANEL: {
        DrawSquarePanelCommandRequest squarePanelRequest = (DrawSquarePanelCommandRequest) beingCreatedComponent;

        Point startPosition = PositionUtils.toCanvasPosition(
            squarePanelRequest.getStartIdxPosition().y, squarePanelRequest.getStartIdxPosition().x);

        DrawUtils.drawLed(gc, properties.getLedStartColor(), startPosition);

        Dimension size = new Dimension(
            mouseCanvasPosition.x - startPosition.x + PositionUtils.SQUARE_LENGTH,
            mouseCanvasPosition.y - startPosition.y + PositionUtils.SQUARE_LENGTH);
        DrawUtils.selectRect(gc, properties.getSelectColor(), startPosition, size);

        Dimension dimension =
            new Dimension(mouseIdxPosition.x - squarePanelRequest.getStartIdxPosition().x + 1,
                mouseIdxPosition.y - squarePanelRequest.getStartIdxPosition().y + 1);
        if (dimension.width > 0 && dimension.height > 0) {
          String sizeText = String.format("[%d, %d]", dimension.width, dimension.height);
          DrawUtils.drawSelectText(gc, properties.getSelectColor(), mouseCanvasPosition, sizeText);
        }
      }
        break;
      case MOVEMENT: {
        MovementCommandRequest movementRequest =
            (MovementCommandRequest) beingCreatedComponent;

        Point start = PositionUtils.toCanvasPosition(movementRequest.getStartIdxPosition());
        Point end = PositionUtils.toCanvasPosition(model.getIdxPointer().getPosition());
        gc.setLineWidth(1);
        gc.setStroke(WHITE);
        gc.setLineDashes(10);
        gc.strokeLine(start.x, start.y, end.x, end.y);
      }
      break;
      case SCALE: {
        ScaleCommandRequest scaleRequest =
            (ScaleCommandRequest) beingCreatedComponent;

        Point start = PositionUtils.toCanvasPosition(scaleRequest.getStartIdxPosition());
        Point end = PositionUtils.toCanvasPosition(model.getIdxPointer().getPosition());
        gc.setLineWidth(1);
        gc.setStroke(WHITE);
        gc.setLineDashes(10);
        gc.strokeRect(start.x, start.y, end.x-start.x, end.y-start.y);
      }
      break;
      default:
        log.error(
            "Invalid current component being created: " + beingCreatedComponent.getCommandType());
    }
  }

  private void drawMousePointer(GraphicsContext gc) {
    Point mouseSquare = PositionUtils
        .toRoundPosition(viewModel.getMousePosition().get());

    switch (model.getConnectionActionState()) {
      case NO_ACTION:
        DrawUtils.drawMousePointer(gc, properties.getNoActionColor(), mouseSquare);
        break;
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
        log.error("Invalid Mouse Pointer (Action): %s", model.getConnectionActionState());
    }
  }
}
