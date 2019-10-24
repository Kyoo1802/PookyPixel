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
  private Point idxPosition;

  public Pointer() {
    canvasPosition = new Point(0, 0);
  }

  public abstract void draw(GraphicsContext gc, ConnectionProperties properties);

  public Point canvasPosition() {
    return canvasPosition;
  }

  public void setCanvasPosition(Point canvasPointerPosition) {
    this.canvasPosition = canvasPointerPosition;
    this.idxPosition = PositionUtils.toIdxPosition(canvasPointerPosition);
  }

  public Point idxPositionCopy() {
    return new Point(idxPosition);
  }
}
