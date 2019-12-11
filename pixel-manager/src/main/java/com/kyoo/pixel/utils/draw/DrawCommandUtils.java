package com.kyoo.pixel.utils.draw;

import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import com.kyoo.pixel.views.stage.fixture.commands.CreateBridgeCommand.CreateBridgeCommandRequest;
import com.kyoo.pixel.views.stage.fixture.commands.CreateLedPathCommand.CreateLedPathCommandRequest;
import com.kyoo.pixel.views.stage.fixture.commands.CreateSquarePanelCommand.CreateSquarePanelCommandRequest;
import com.kyoo.pixel.views.stage.fixture.commands.MoveComponentCommand.MoveCommandRequest;
import com.kyoo.pixel.views.stage.fixture.commands.ScaleComponentCommand.ScaleCommandRequest;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

import static com.kyoo.pixel.utils.draw.DrawComponentUtils.getBridgePosition;
import static com.kyoo.pixel.utils.draw.DrawUtils.*;

public final class DrawCommandUtils {

  public static void drawSquarePanelCommand(
      GraphicsContext gc,
      FixtureProperties properties,
      CreateSquarePanelCommandRequest request,
      Point pointer) {
    Point startPosition =
        PositionUtils.toCanvasPosition(
            request.getStartIdxPosition().y, request.getStartIdxPosition().x);

    // Draw first led
    drawLed(gc, properties.getLedStartColor(), startPosition);

    // Draw component preview
    Point mouseCanvasPosition = PositionUtils.toCanvasPosition(pointer);
    Dimension size =
        new Dimension(
            mouseCanvasPosition.x - startPosition.x + PositionUtils.SQUARE_LENGTH,
            mouseCanvasPosition.y - startPosition.y + PositionUtils.SQUARE_LENGTH);
    drawSelectionRect(gc, properties.getSelectColor(), startPosition, size);

    // Draw Text info
    Dimension dimension =
        new Dimension(
            pointer.x - request.getStartIdxPosition().x + 1,
            pointer.y - request.getStartIdxPosition().y + 1);
    if (dimension.width > 0 && dimension.height > 0) {
      String sizeText = String.format("[%d, %d]", dimension.width, dimension.height);
      DrawUtils.drawText(gc, properties.getSelectColor(), mouseCanvasPosition, sizeText);
    }
  }

  public static void drawLedPathCommand(
      GraphicsContext gc,
      FixtureProperties properties,
      CreateLedPathCommandRequest request,
      Point pointer) {
    Point minPoint = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    Point maxPoint = new Point(0, 0);

    gc.setLineWidth(1);
    gc.setLineDashes();
    gc.beginPath();
    int i = 0;

    // Draw Leds
    for (Point p : request.getIdxPositions()) {
      Point startPosition = PositionUtils.toCanvasPosition(p.y, p.x);
      minPoint.x = Math.min(minPoint.x, startPosition.x);
      minPoint.y = Math.min(minPoint.y, startPosition.y);
      maxPoint.x = Math.max(maxPoint.x, startPosition.x);
      maxPoint.y = Math.max(maxPoint.y, startPosition.y);
      if (i == 0) {
        gc.moveTo(
            startPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
            startPosition.y + PositionUtils.HALF_SQUARE_LENGTH);
        DrawUtils.drawLed(gc, properties.getLedStartColor(), startPosition);
      } else {
        gc.lineTo(
            startPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
            startPosition.y + PositionUtils.HALF_SQUARE_LENGTH);
        DrawUtils.drawLed(gc, properties.getLedOffColor(), startPosition);
      }
      i++;
    }
    Point canvasPosition = PositionUtils.toCanvasPosition(pointer);
    gc.lineTo(
        canvasPosition.x + PositionUtils.HALF_SQUARE_LENGTH,
        canvasPosition.y + PositionUtils.HALF_SQUARE_LENGTH);
    gc.setStroke(Color.web(properties.getLedConnectionPathColor()));
    gc.stroke();

    // Draw component preview
    Dimension size =
        new Dimension(
            maxPoint.x - minPoint.x + PositionUtils.SQUARE_LENGTH,
            maxPoint.y - minPoint.y + PositionUtils.SQUARE_LENGTH);
    drawSelectionRect(gc, properties.getSelectColor(), minPoint, size);

    // Draw component info
    String sizeText = String.format("[%d]", request.getIdxPositions().size());
    drawText(gc, properties.getSelectColor(), maxPoint, sizeText);
  }

  public static void drawBridgeCommand(
      GraphicsContext gc,
      FixtureProperties properties,
      CreateBridgeCommandRequest request,
      Point pointer) {
    Point start = getBridgePosition(request.getStartComponent(), true);
    Point end = PositionUtils.toCanvasPosition(pointer);
    gc.setLineWidth(2);
    gc.setStroke(Color.web(properties.getBridgeColor()));
    gc.setLineDashes();
    gc.strokeLine(
        start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH,
        end.x + PositionUtils.HALF_SQUARE_LENGTH,
        end.y + PositionUtils.HALF_SQUARE_LENGTH);
  }

  public static void drawMoveCommand(
      GraphicsContext gc, FixtureProperties properties, MoveCommandRequest request, Point pointer) {
    Point start = PositionUtils.toCanvasPosition(request.getStartIdxPosition());
    Point end = PositionUtils.toCanvasPosition(pointer);
    gc.setLineWidth(1);
    gc.setStroke(Color.web(properties.getSelectColor()));
    gc.setLineDashes(10);
    gc.strokeLine(
        start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH,
        end.x + PositionUtils.HALF_SQUARE_LENGTH,
        end.y + PositionUtils.HALF_SQUARE_LENGTH);
  }

  public static void drawScaleCommand(
      GraphicsContext gc,
      FixtureProperties properties,
      ScaleCommandRequest request,
      Point idxPointer) {

    // Draw Scale square
    Point start = PositionUtils.toCanvasPosition(request.getComponentStartIdxPoint());
    Dimension dimension =
        PositionUtils.toCanvasDimension(
            new Dimension(
                idxPointer.x - request.getComponentStartIdxPoint().x,
                idxPointer.y - request.getComponentStartIdxPoint().x));
    Point end = PositionUtils.toCanvasPosition(idxPointer);

    gc.setLineWidth(properties.getSelectWidth());
    gc.setStroke(Color.web(properties.getSelectColor()));
    gc.setLineDashes(10);
    gc.strokeRect(
        start.x + PositionUtils.HALF_SQUARE_LENGTH,
        start.y + PositionUtils.HALF_SQUARE_LENGTH,
        dimension.width,
        dimension.height);

    // Draw scale info
    if (dimension.width > 0 && dimension.height > 0) {
      String sizeText =
          String.format(
              "[%d, %d] = %d",
              dimension.width, dimension.height, dimension.width * dimension.height);
      DrawUtils.drawText(gc, properties.getSelectColor(), end, sizeText);
    }
  }
}
