package com.kyoo.pixel.views.stage.fixture.paint;

import com.kyoo.pixel.views.common.paint.MovableObject;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public final class InteractiveGroup implements MovableObject {

  private List<InteractiveComponent> components;

  private InteractiveGroup() {
    components = new LinkedList<>();
  }

  public void addComponent(InteractiveComponent component) {
    components.add(component);
  }

  public void removeComponent(InteractiveComponent component) {
    components.remove(component);
  }

  @Override
  public void move(Point movement) {
    for (InteractiveComponent component : components) {
      component.move(movement);
    }
  }
}
