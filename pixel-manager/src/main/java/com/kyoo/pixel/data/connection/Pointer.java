package com.kyoo.pixel.data.connection;

import java.awt.Point;
import lombok.Data;

@Data
public class Pointer {

  private Point position;

  public Pointer(Point position) {
    this.position = position;
  }

  public void setLocation(Point actionPosition) {
    position.setLocation(actionPosition);
  }
}
