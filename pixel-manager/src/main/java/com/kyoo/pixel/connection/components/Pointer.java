package com.kyoo.pixel.connection.components;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import lombok.Data;

@Data
public abstract class Pointer {

  private Point idxPosition;

  public Pointer() {
    idxPosition = new Point(0, 0);
  }

  public abstract void draw(GraphicsContext gc, ConnectionProperties properties);

  public Point canvasPosition() {
    return PositionUtils.toCanvasPosition(idxPosition);
  }
}
