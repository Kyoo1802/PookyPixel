package com.kyoo.pixel.utils;

import static com.kyoo.pixel.utils.DrawComponentUtils.drawLedPath;
import static com.kyoo.pixel.utils.DrawComponentUtils.getBridgePosition;
import static com.kyoo.pixel.utils.DrawUtils.drawLed;
import static com.kyoo.pixel.utils.DrawUtils.drawSelectionRect;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.commands.CreateBridgeCommand.CreateBridgeCommandRequest;
import com.kyoo.pixel.connection.components.commands.CreateLedPathCommand.CreateLedPathCommandRequest;
import com.kyoo.pixel.connection.components.commands.CreateSquarePanelCommand.CreateSquarePanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.MoveComponentCommand.MovementCommandRequest;
import com.kyoo.pixel.connection.components.commands.ScaleComponentCommand.ScaleCommandRequest;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Optional;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class DrawCommandUtils {


  public static void drawSquarePanelCommand(GraphicsContext gc, ConnectionProperties properties,
      CreateSquarePanelCommandRequest request, Point pointer) {
    Point startPosition = PositionUtils.toCanvasPosition(
        request.getStartIdxPosition().y, request.getStartIdxPosition().x);

    // Draw first led
    drawLed(gc, properties.getLedStartColor(), startPosition);

    // Draw component preview
    Point mouseCanvasPosition = PositionUtils.toCanvasPosition(pointer);
    Dimension size = new Dimension(
        mouseCanvasPosition.x - startPosition.x + PositionUtils.SQUARE_LENGTH,
        mouseCanvasPosition.y - startPosition.y + PositionUtils.SQUARE_LENGTH);
    drawSelectionRect(gc, properties.getSelectColor(), startPosition, size);

    // Draw Text info
    Dimension dimension =
        new Dimension(pointer.x - request.getStartIdxPosition().x + 1,
            pointer.y - request.getStartIdxPosition().y + 1);
    if (dimension.width > 0 && dimension.height > 0) {
      String sizeText = String.format("[%d, %d]", dimension.width, dimension.height);
      DrawUtils.drawText(gc, properties.getSelectColor(), mouseCanvasPosition, sizeText);
    }
  }

  public static void drawLedPathCommand(GraphicsContext gc, ConnectionProperties properties,
      CreateLedPathCommandRequest request, Point pointer) {
    drawLedPath(gc, properties, request.getIdxPositions(), Optional.of(pointer));
  }

  public static void drawBridgeCommand(GraphicsContext gc, ConnectionProperties properties,
      CreateBridgeCommandRequest request, Point pointer) {
    Point start = getBridgePosition(request.getStartComponent(), true);
    Point end = PositionUtils.toCanvasPosition(pointer);
    gc.setLineWidth(2);
    gc.setStroke(Color.web(properties.getBridgeColor()));
    gc.setLineDashes();
    gc.strokeLine(start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH,
        end.x + PositionUtils.HALF_SQUARE_LENGTH, end.y + PositionUtils.HALF_SQUARE_LENGTH);
  }

  public static void drawMovementCommand(GraphicsContext gc, ConnectionProperties properties,
      MovementCommandRequest request, Point pointer) {
    Point start = PositionUtils.toCanvasPosition(request.getStartIdxPosition());
    Point end = PositionUtils.toCanvasPosition(pointer);
    gc.setLineWidth(1);
    gc.setStroke(Color.web(properties.getSelectColor()));
    gc.setLineDashes(10);
    gc.strokeLine(start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH,
        end.x + PositionUtils.HALF_SQUARE_LENGTH, end.y + PositionUtils.HALF_SQUARE_LENGTH);
  }

  public static void drawScaleCommand(GraphicsContext gc, ConnectionProperties properties,
      ScaleCommandRequest request, Point idxPointer) {

    // Draw Scale square
    Point start = PositionUtils.toCanvasPosition(request.getComponentStartIdxPoint());
    Dimension dimension =
        PositionUtils
            .toCanvasDimension(new Dimension(idxPointer.x - request.getComponentStartIdxPoint().x,
                idxPointer.y - request.getComponentStartIdxPoint().x));
    Point end = PositionUtils.toCanvasPosition(idxPointer);

    gc.setLineWidth(properties.getSelectWidth());
    gc.setStroke(Color.web(properties.getSelectColor()));
    gc.setLineDashes(10);
    gc.strokeRect(start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH, dimension.width, dimension.height);

    // Draw scale info
    if (dimension.width > 0 && dimension.height > 0) {
      String sizeText = String
          .format("[%d, %d] = %d", dimension.width, dimension.height,
              dimension.width * dimension.height);
      DrawUtils.drawText(gc, properties.getSelectColor(), end, sizeText);
    }
  }
}
