package com.kyoo.pixel.fixtures.pointers;

import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.utils.PositionUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class DefaultPointer extends Pointer {

  @Override
  public void draw(GraphicsContext gc, FixtureProperties properties) {
    gc.setLineWidth(properties.getMouseWidth());
    gc.setStroke(Color.web(properties.getMouseColor()));
    gc.setLineDashes();
    gc.strokeRect(getIdxCanvasPosition().x - 2, getIdxCanvasPosition().y - 2,
        PositionUtils.SQUARE_LENGTH + 4,
        PositionUtils.SQUARE_LENGTH + 4);
  }
}
