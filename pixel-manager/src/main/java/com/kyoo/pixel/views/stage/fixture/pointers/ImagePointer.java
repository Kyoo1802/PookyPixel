package com.kyoo.pixel.views.stage.fixture.pointers;

import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class ImagePointer extends Pointer {

  @Override
  public void draw(GraphicsContext gc, FixtureProperties properties) {
    gc.setLineWidth(properties.getMouseWidth());
    gc.setStroke(Color.web(properties.getMouseColor()));
    gc.fillOval(getCanvasPosition().x, getCanvasPosition().y, 20, 20);
  }
}
