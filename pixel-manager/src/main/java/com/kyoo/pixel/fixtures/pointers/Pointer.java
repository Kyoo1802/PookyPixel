package com.kyoo.pixel.fixtures.pointers;

import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

public abstract class Pointer {

  @Getter
  private Point canvasPosition;
  @Getter
  private Point idxCanvasPosition;
  @Getter
  private Point idxPosition;

  public Pointer() {
    canvasPosition = new Point(0, 0);
    idxCanvasPosition = new Point(0, 0);
  }

  public abstract void draw(GraphicsContext gc, FixtureProperties properties);

  public void setCanvasPosition(Point canvasPointerPosition) {
    this.canvasPosition = canvasPointerPosition;
    Point tmpPosition = new Point(this.canvasPosition.x + PositionUtils.HALF_SQUARE_LENGTH - 2,
        this.canvasPosition.y + PositionUtils.HALF_SQUARE_LENGTH - 1);
    this.idxPosition = PositionUtils.toIdxPosition(tmpPosition);
    this.idxCanvasPosition = PositionUtils.toCanvasPosition(idxPosition);
  }

  public Point idxPositionCopy() {
    return new Point(idxPosition);
  }
}
