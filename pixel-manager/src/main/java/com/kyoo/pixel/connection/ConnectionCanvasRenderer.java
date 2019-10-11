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

  private ConnectionViewModel connectionViewModel;
  private BufferedImage background;
  private ConnectionProperties connectionProperties;

  @Inject
  public ConnectionCanvasRenderer(ConnectionViewModel connectionViewModel,
      ConnectionProperties connectionProperties) {
    this.connectionViewModel = connectionViewModel;
    this.connectionViewModel.getCanvasWidth().addListener(v -> recreateBackground());
    this.connectionViewModel.getCanvasHeight().addListener(v -> recreateBackground());
    this.connectionProperties = connectionProperties;
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
    Dimension canvasDimension = new Dimension(connectionViewModel.getCanvasWidth().get(),
        connectionViewModel.getCanvasHeight().get());
    if (canvasDimension.width * canvasDimension.height == 0) {
      return;
    }

    BufferedImage bufferedImage =
        new BufferedImage(canvasDimension.width, canvasDimension.height,
            BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
    DrawUtils.drawBackground(graphics2D, connectionProperties.getBackgroundColor(),
        connectionProperties.getBackgroundDotsColor(), canvasDimension);
    graphics2D.dispose();

    background = bufferedImage;
  }

  private void createdComponents(GraphicsContext gc) {
    if (connectionViewModel.getConnectionModel().getCreatedComponents().all().isEmpty()) {
      return;
    }
    Optional<ConnectionComponent> selectedComponent =
        connectionViewModel.getConnectionModel().getSelectedComponent();
    for (Map<Long, ConnectionComponent> components :
        connectionViewModel.getConnectionModel().getCreatedComponents().all().values()) {
      for (ConnectionComponent component : components.values()) {
        if (selectedComponent.isPresent() && selectedComponent.get() == component) {

          String sizeText = String
              .format("[%d, %d]",
                  component.getEndIdxPosition().x - component.getStartIdxPosition().x + 1,
                  component.getEndIdxPosition().y - component.getStartIdxPosition().y + 1);

          Point startCanvasPosition = PositionUtils
              .toCanvasPosition(component.getStartIdxPosition());
          Point endCanvasPosition = PositionUtils.toCanvasPosition(component.getEndIdxPosition());

          DrawUtils.drawMouseText(gc, connectionProperties.getSelectColor(), endCanvasPosition,
              sizeText);
          Dimension selectSize = new Dimension(
              endCanvasPosition.x - startCanvasPosition.x + PositionUtils.HALF_SQUARE_LENGTH * 3,
              endCanvasPosition.y - startCanvasPosition.y + PositionUtils.HALF_SQUARE_LENGTH * 3);
          DrawUtils
              .selectRect(gc, connectionProperties.getSelectColor(), startCanvasPosition,
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
                color = connectionProperties.getLedStartColor();
              } else if (idx == sp.getLeds().size() - 1) {
                color = connectionProperties.getLedEndColor();
              } else {
                color = connectionProperties.getLedOffColor();
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
    if (connectionViewModel.getConnectionModel().getBeingCreatedComponent().isEmpty()) {
      return;
    }

    ConnectionCommandRequest beingCreatedComponent =
        connectionViewModel.getConnectionModel().getBeingCreatedComponent().get();
    Point mouseCanvasPosition = PositionUtils
        .toRoundPosition(connectionViewModel.getMousePosition().get());
    Point mouseIdxPosition =
        PositionUtils.toIdxPosition(new Point(mouseCanvasPosition.x, mouseCanvasPosition.y));

    switch (beingCreatedComponent.getComponentType()) {
      case SQUARE_PANEL:
        DrawPanelCommandRequest panel = (DrawPanelCommandRequest) beingCreatedComponent;
        Point panelCanvasPosition = PositionUtils.toCanvasPosition(
            panel.getStartIdxPosition().y, panel.getStartIdxPosition().x);

        DrawUtils.drawLed(gc, connectionProperties.getLedStartColor(), panelCanvasPosition);

        String sizeText = String
            .format("[%d, %d]", mouseIdxPosition.x - panel.getStartIdxPosition().x + 1,
                mouseIdxPosition.y - panel.getStartIdxPosition().y + 1);
        DrawUtils.drawMouseText(gc, connectionProperties.getSelectColor(), mouseCanvasPosition,
            sizeText);

        Point selectPosition = new Point(panelCanvasPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
            panelCanvasPosition.y + PositionUtils.HALF_SQUARE_LENGTH);
        Dimension selectSize = new Dimension(mouseCanvasPosition.x - panelCanvasPosition.x,
            mouseCanvasPosition.y - panelCanvasPosition.y);
        DrawUtils.selectRect(gc, connectionProperties.getSelectColor(), selectPosition, selectSize);
      default:
    }
  }

  private void drawMousePointer(GraphicsContext gc) {
    Point mouseSquare = PositionUtils
        .toRoundPosition(connectionViewModel.getMousePosition().get());

    switch (connectionViewModel.getConnectionModel().getConnectionAction()) {
      case NO_ACTION:
        DrawUtils.drawMousePointer(gc, connectionProperties.getNoActionColor(), mouseSquare);
        break;
      case DRAW:
        switch (connectionViewModel.getConnectionModel().getDrawAction()) {
          case DRAW_SQUARE_PANEL:
            if (connectionViewModel.getConnectionModel().getBeingCreatedComponent().isEmpty()) {
              DrawUtils.drawLed(gc, connectionProperties.getLedStartColor(), mouseSquare);
            } else {
              DrawUtils.drawLed(gc, connectionProperties.getLedEndColor(), mouseSquare);
            }
            break;
          default:
        }
        break;
      default:
    }
  }
}
