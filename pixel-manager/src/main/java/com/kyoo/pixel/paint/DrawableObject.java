package com.kyoo.pixel.paint;

import com.kyoo.pixel.fixtures.FixtureProperties;
import javafx.scene.canvas.GraphicsContext;

public interface DrawableObject {

  void draw(GraphicsContext gc, FixtureProperties properties);

  void drawComponent(GraphicsContext gc, FixtureProperties properties);
}
