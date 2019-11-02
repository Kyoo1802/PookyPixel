package com.kyoo.pixel.fixtures.components;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class BasicComponentUnit implements ComponentUnit {

  @Getter
  protected ComponentKey key;
  @Getter
  protected Map<String, Object> properties;
  @Setter
  @Getter
  protected Point position;
  @Setter
  @Getter
  protected Dimension size;

  protected BasicComponentUnit(long id, ComponentType componentType) {
    this.key = new ComponentKey(id, componentType);
    this.properties = new HashMap<>();
    this.position = new Point(0, 0);
    this.size = new Dimension(1, 1);
  }

  @Override
  public ComponentType type() {
    return key.getType();
  }

  @Override
  public Point getEndPosition() {
    return new Point(position.x + size.width - 1, position.y + size.height - 1);
  }

  @Override
  public boolean intersects(Point point) {
    return position.x <= point.x && point.x < position.x + size.width - 1
        && position.y <= point.y && point.y < position.y + size.height - 1;
  }
}
