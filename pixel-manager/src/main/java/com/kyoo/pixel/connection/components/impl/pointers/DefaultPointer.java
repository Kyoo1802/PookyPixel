package com.kyoo.pixel.connection.components.impl.pointers;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.Pointer;
import com.kyoo.pixel.utils.PositionUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class DefaultPointer extends Pointer {

  @Override
  public void draw(GraphicsContext gc, ConnectionProperties properties) {
    gc.setLineWidth(properties.getMouseWidth());
    gc.setStroke(Color.web(properties.getMouseColor()));
    gc.setLineDashes();
    gc.strokeRect(getCanvasPosition().x, getCanvasPosition().y, PositionUtils.SQUARE_LENGTH + 4,
        PositionUtils.SQUARE_LENGTH + 4);
  }
}
