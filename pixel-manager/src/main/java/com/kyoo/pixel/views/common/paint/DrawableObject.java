package com.kyoo.pixel.views.common.paint;

import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import javafx.scene.canvas.GraphicsContext;

public interface DrawableObject {

  void draw(GraphicsContext gc, FixtureProperties properties);

  void drawComponent(GraphicsContext gc, FixtureProperties properties);
}
