package com.kyoo.pixel.connection.components;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

public abstract class Pointer {

  @Getter
  private Point canvasPosition;
  @Getter
  private Point displayPosition;
  @Getter
  private Point idxPosition;

  public Pointer() {
    canvasPosition = new Point(0, 0);
    displayPosition = new Point(0, 0);
  }

  public abstract void draw(GraphicsContext gc, ConnectionProperties properties);

  public void setCanvasPosition(Point canvasPointerPosition) {
    this.canvasPosition = canvasPointerPosition;
    Point tmpPosition = new Point(this.canvasPosition.x+PositionUtils.HALF_SQUARE_LENGTH - 2,
        this.canvasPosition.y+PositionUtils.HALF_SQUARE_LENGTH - 1);
    this.idxPosition = PositionUtils.toIdxPosition(tmpPosition);
    this.displayPosition = PositionUtils.toCanvasPosition(idxPosition);
  }

  public Point idxPositionCopy() {
    return new Point(idxPosition);
  }
}
