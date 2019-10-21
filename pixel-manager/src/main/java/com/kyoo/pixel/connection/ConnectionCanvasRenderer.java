package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.kyoo.pixel.connection.components.Bridge;
import com.kyoo.pixel.connection.components.DriverPort;
import com.kyoo.pixel.connection.components.LedPath;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.connection.components.SquarePanel;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawBridgeCommandRequest;
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
import javafx.scene.image.WritableImage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ConnectionCanvasRenderer {

  private ConnectionModel model;
  private BufferedImage background;
  private ConnectionProperties properties;
  private WritableImage fxBackground;

  @Inject
  public ConnectionCanvasRenderer(ConnectionViewModel viewModel,
      ConnectionProperties properties) {
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
    // If there is a background image and canvas dimension hasn't change, draw the background image
    if (fxBackground != null) {
      log.debug("Draw background");
      gc.drawImage(fxBackground, 0, 0);
      return;
    } else {
      log.debug("Recreate background: " + model.getDimension());
      recreateBackground();
      gc.drawImage(fxBackground, 0, 0);
    }
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
    fxBackground = SwingFXUtils.toFXImage(background, null);
  }

  private void createdComponents(GraphicsContext gc) {
    for (Map<Long, SelectableComponent> components :
        model.getCreatedComponentsManager().getComponents().values()) {
      for (SelectableComponent component : components.values()) {
        // Draw Component
        switch (component.getComponentType()) {
          case SQUARE_PANEL:
            DrawUtils.drawSquarePanel(gc, properties, (SquarePanel) component);
            break;
          case DRIVER_PORT:
            DrawUtils.drawPort(gc, properties, (DriverPort) component);
            break;
          case BRIDGE:
            DrawUtils.drawBridge(gc, properties, (Bridge) component);
            break;
          case LED_PATH:
            DrawUtils.drawLedPath(gc, properties, (LedPath) component,
                model.getPointer());
            break;
          default:
            log.error("Invalid component to draw: " + component.getComponentType());
        }
        // Draw Selection
        Optional<SelectableComponent> selectedComponent = model.getSelectedComponent();
        if (selectedComponent.isPresent() && selectedComponent.get() == component) {
          DrawUtils.drawComponentSelection(gc, properties, component);
        }
      }
    }
  }

  private void currentComponent(GraphicsContext gc) {
    if (model.getActiveCommandRequest().isEmpty()) {
      return;
    }

    ConnectionCommandRequest component = model.getActiveCommandRequest().get();

    switch (component.getCommandType()) {
      case SQUARE_PANEL:
        DrawUtils.drawSquarePanelCommand(gc, properties, (DrawSquarePanelCommandRequest) component,
            model.getPointer());
        break;
      case LED_PATH:
        DrawUtils.drawLedPathCommand(gc, properties, (DrawLedPathCommandRequest) component,
            model.getPointer());
        break;
      case BRIDGE:
        DrawUtils
            .drawBridgeCommand(gc, properties, (DrawBridgeCommandRequest) component,
                model.getPointer());
        break;
      case MOVEMENT:
        DrawUtils.drawMovementCommand(gc, properties, (MovementCommandRequest) component,
            model.getPointer());
        break;
      case SCALE:
        ScaleCommandRequest request = (ScaleCommandRequest) component;
        SelectableComponent selectedComponent =
            model.getCreatedComponentsManager().getComponent(request.getTypeToScale(),
                request.getIdToScale()).get();
        DrawUtils.drawScaleCommand(gc, properties, request, model.getPointer(),
            selectedComponent.getSize());
        break;
      default:
        log.error("Invalid current component being created: " + component.getCommandType());
    }
  }

  private void drawMousePointer(GraphicsContext gc) {
    Point mouseSquare = PositionUtils.toCanvasPosition(model.getPointer());

    switch (model.getConnectionState()) {
      case NO_ACTION:
        DrawUtils.drawDefaultPointer(gc, properties, mouseSquare);
        break;
      case DRAW_LED_PATH:
      case DRAW_SQUARE_PANEL:
        if (model.getActiveCommandRequest().isEmpty()) {
          DrawUtils.drawLed(gc, properties.getLedStartColor(), mouseSquare);
        } else {
          DrawUtils.drawLed(gc, properties.getLedEndColor(), mouseSquare);
        }
        break;
      case DRAW_LED_BRIDGE:
      case DRAW_DRIVER_PORT:
        DrawUtils.drawDriverPortPointer(gc, properties, mouseSquare);
        break;
      default:
        log.error("Invalid Mouse Pointer (Action): " + model.getConnectionState());
    }
  }
}
