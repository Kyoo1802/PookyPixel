package com.kyoo.pixel.views.stage.fixture.paint;

import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import com.kyoo.pixel.views.stage.fixture.components.led.Led;
import javafx.scene.canvas.GraphicsContext;

public class InteractiveLed extends InteractiveComponent {

  public InteractiveLed(Led squarePanel) {
    super(squarePanel);
  }

  @Override
  public void drawComponent(GraphicsContext gc, FixtureProperties properties) {}
}
