package com.kyoo.pixel.connection.components.impl.pointers;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.Pointer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class ImagePointer extends Pointer {

  @Override
  public void draw(GraphicsContext gc, ConnectionProperties properties) {
    gc.setLineWidth(properties.getMouseWidth());
    gc.setStroke(Color.web(properties.getMouseColor()));
    gc.fillOval(getCanvasPosition().x, getCanvasPosition().y, 20, 20);
  }
}