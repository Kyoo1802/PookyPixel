package com.kyoo.pixel.views.stage.fixture.commands;

import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public abstract class FixtureCommandRequest {

  public abstract void draw(GraphicsContext gc, FixtureProperties properties, Point pointer);
}
