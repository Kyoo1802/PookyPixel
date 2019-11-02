package com.kyoo.pixel.fixtures.commands;

import com.kyoo.pixel.fixtures.FixtureProperties;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;

public abstract class FixtureCommandRequest {

  public abstract void draw(GraphicsContext gc, FixtureProperties properties, Point pointer);
}
