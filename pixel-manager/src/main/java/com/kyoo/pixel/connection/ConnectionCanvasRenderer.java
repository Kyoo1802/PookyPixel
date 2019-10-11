package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.Led;
import com.kyoo.pixel.connection.components.SquarePanel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawPanelCommandRequest;
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
  private BufferedImage background;
  private ConnectionProperties properties;

  @Inject
  public ConnectionCanvasRenderer(ConnectionViewModel viewModel,
      ConnectionProperties properties) {
    this.viewModel = viewModel;
    this.viewModel.getCanvasWidth().addListener(v -> recreateBackground());
    this.viewModel.getCanvasHeight().addListener(v -> recreateBackground());
    this.properties = properties;
  }

  private static Point getLedPanelPosition(Point canvasPosition) {
    return new Point(canvasPosition.x + PositionUtils.HALF_SQUARE_LENGTH / 2,
        canvasPosition.y + PositionUtils.HALF_SQUARE_LENGTH / 2);
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
      gc.drawImage(
          SwingFXUtils.toFXImage(background, null), 0, 0);
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
    if (viewModel.getModel().getCreatedComponentsManager().all().isEmpty()) {
      return;
    }
    Optional<ConnectionComponent> selectedComponent =
        viewModel.getModel().getSelectedComponent();
    for (Map<Long, ConnectionComponent> components :
        viewModel.getModel().getCreatedComponentsManager().all().values()) {
      for (ConnectionComponent component : components.values()) {
        if (selectedComponent.isPresent() && selectedComponent.get() == component) {

          String sizeText = String
              .format("[%d, %d]",
                  component.getEndIdxPosition().x - component.getStartIdxPosition().x + 1,
                  component.getEndIdxPosition().y - component.getStartIdxPosition().y + 1);

          Point startCanvasPosition = PositionUtils
              .toCanvasPosition(component.getStartIdxPosition());
          Point endCanvasPosition = PositionUtils.toCanvasPosition(component.getEndIdxPosition());

          DrawUtils.drawMouseText(gc, properties.getSelectColor(), endCanvasPosition,
              sizeText);
          Dimension selectSize = new Dimension(
              endCanvasPosition.x - startCanvasPosition.x + PositionUtils.HALF_SQUARE_LENGTH * 3,
              endCanvasPosition.y - startCanvasPosition.y + PositionUtils.HALF_SQUARE_LENGTH * 3);
          DrawUtils
              .selectRect(gc, properties.getSelectColor(), startCanvasPosition,
                  selectSize);
        }
        switch (component.getConnectionType()) {
          case SQUARE_PANEL:
            SquarePanel sp = (SquarePanel) component;
            int idx = 0;
            for (Led led : sp.getLeds().values()) {
              Point ledCanvasPosition = PositionUtils
                  .toCanvasPosition(led.getIdxPosition().y, led.getIdxPosition().x);
              String color;
              if (idx == 0) {
                color = properties.getLedStartColor();
              } else if (idx == sp.getLeds().size() - 1) {
                color = properties.getLedEndColor();
              } else {
                color = properties.getLedOffColor();
              }
              DrawUtils.drawLed(gc, color, getLedPanelPosition(ledCanvasPosition));
              idx++;
            }
          default:
        }
      }
    }
  }

  private void currentComponent(GraphicsContext gc) {
    if (viewModel.getModel().getBeingCreatedComponent().isEmpty()) {
      return;
    }

    ConnectionCommandRequest beingCreatedComponent =
        viewModel.getModel().getBeingCreatedComponent().get();
    Point mouseCanvasPosition = PositionUtils
        .toRoundPosition(viewModel.getMousePosition().get());
    Point mouseIdxPosition =
        PositionUtils.toIdxPosition(new Point(mouseCanvasPosition.x, mouseCanvasPosition.y));

    switch (beingCreatedComponent.getComponentType()) {
      case SQUARE_PANEL:
        DrawPanelCommandRequest squarePanelRequest = (DrawPanelCommandRequest) beingCreatedComponent;
        Point panelCanvasPosition = PositionUtils.toCanvasPosition(
            squarePanelRequest.getStartIdxPosition().y, squarePanelRequest.getStartIdxPosition().x);

        DrawUtils.drawLed(gc, properties.getLedStartColor(), panelCanvasPosition);

        String sizeText = String
            .format("[%d, %d]", mouseIdxPosition.x - squarePanelRequest.getStartIdxPosition().x + 1,
                mouseIdxPosition.y - squarePanelRequest.getStartIdxPosition().y + 1);
        DrawUtils.drawMouseText(gc, properties.getSelectColor(), mouseCanvasPosition,
            sizeText);

        Point selectPosition = new Point(panelCanvasPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
            panelCanvasPosition.y + PositionUtils.HALF_SQUARE_LENGTH);
        Dimension selectSize = new Dimension(mouseCanvasPosition.x - panelCanvasPosition.x,
            mouseCanvasPosition.y - panelCanvasPosition.y);
        DrawUtils.selectRect(gc, properties.getSelectColor(), selectPosition, selectSize);
        break;
      default:
        log.error(
            "Invalid current component being created: " + beingCreatedComponent.getComponentType());
    }
  }

  private void drawMousePointer(GraphicsContext gc) {
    Point mouseSquare = PositionUtils
        .toRoundPosition(viewModel.getMousePosition().get());

    switch (viewModel.getModel().getConnectionActionState()) {
      case NO_ACTION:
        DrawUtils.drawMousePointer(gc, properties.getNoActionColor(), mouseSquare);
        break;
      case DRAW:
        switch (viewModel.getModel().getDrawActionState()) {
          case DRAW_SQUARE_PANEL:
            if (viewModel.getModel().getBeingCreatedComponent().isEmpty()) {
              DrawUtils.drawLed(gc, properties.getLedStartColor(), mouseSquare);
            } else {
              DrawUtils.drawLed(gc, properties.getLedEndColor(), mouseSquare);
            }
            break;
          default:
        }
        break;
      default:
    }
  }
}
