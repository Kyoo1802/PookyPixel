package com.kyoo.pixel.views.stage.fixture.pointers;

import com.kyoo.pixel.utils.draw.PositionUtils;
import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import javafx.scene.canvas.GraphicsContext;

public final class LedComponentPointer extends Pointer {

  @Override
  public void draw(GraphicsContext gc, FixtureProperties properties) {
    gc.fillOval(
        getCanvasPosition().x,
        getCanvasPosition().y,
        PositionUtils.SQUARE_LENGTH,
        PositionUtils.SQUARE_LENGTH);
  }
}
