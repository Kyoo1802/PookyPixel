package com.kyoo.pixel.connection.components.impl.pointers;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.Pointer;
import com.kyoo.pixel.utils.PositionUtils;
import javafx.scene.canvas.GraphicsContext;

public final class LedComponentPointer extends Pointer {

  @Override
  public void draw(GraphicsContext gc, ConnectionProperties properties) {
    gc.fillOval(getCanvasPosition().x, getCanvasPosition().y, PositionUtils.SQUARE_LENGTH,
        PositionUtils.SQUARE_LENGTH);
  }
}