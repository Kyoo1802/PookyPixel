package com.kyoo.pixel.connection.components.impl.pointers;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.Pointer;
import com.kyoo.pixel.utils.DrawUtils;
import javafx.scene.canvas.GraphicsContext;

public final class DefaultPointer extends Pointer {

  @Override
  public void draw(GraphicsContext gc, ConnectionProperties properties) {
    DrawUtils.drawDefaultPointer(gc, properties, this.canvasPosition());
  }
}
