package com.kyoo.pixel.fixtures.pointers;

import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.utils.PositionUtils;
import javafx.scene.canvas.GraphicsContext;

public final class LedComponentPointer extends Pointer {

  @Override
  public void draw(GraphicsContext gc, FixtureProperties properties) {
    gc.fillOval(getCanvasPosition().x, getCanvasPosition().y, PositionUtils.SQUARE_LENGTH,
        PositionUtils.SQUARE_LENGTH);
  }
}