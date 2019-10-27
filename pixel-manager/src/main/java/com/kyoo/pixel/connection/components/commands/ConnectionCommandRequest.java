package com.kyoo.pixel.connection.components.commands;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;

public abstract class ConnectionCommandRequest {

  public abstract ComponentType getCommandType();

  public abstract void draw(GraphicsContext gc, ConnectionProperties properties, Point pointer);
}
