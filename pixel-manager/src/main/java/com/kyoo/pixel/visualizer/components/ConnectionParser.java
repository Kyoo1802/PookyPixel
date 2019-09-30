package com.kyoo.pixel.visualizer.components;

import com.kyoo.pixel.visualizer.PixelConnection;
import com.kyoo.pixel.visualizer.data.PixelFrame;
import javax.inject.Inject;

public final class ConnectionParser {
  private PixelConnection connection;

  @Inject
  public ConnectionParser(PixelConnection connection){
    this.connection = connection;
  }

  public PixelFrame parse(PixelFrame frame) {
    return null;
  }

  public void update(PixelConnection connection) {
    this.connection = connection;
  }
}
