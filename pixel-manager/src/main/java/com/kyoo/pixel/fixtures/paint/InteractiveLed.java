package com.kyoo.pixel.fixtures.paint;

import com.kyoo.pixel.fixtures.FixtureProperties;
import com.kyoo.pixel.fixtures.components.led.Led;
import javafx.scene.canvas.GraphicsContext;

public class InteractiveLed extends InteractiveComponent {

  public InteractiveLed(Led squarePanel) {
    super(squarePanel);
  }

  @Override
  public void drawComponent(GraphicsContext gc, FixtureProperties properties) {
  }
}
