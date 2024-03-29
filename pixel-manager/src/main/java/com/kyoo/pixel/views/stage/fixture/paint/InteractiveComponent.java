package com.kyoo.pixel.views.stage.fixture.paint;

import com.kyoo.pixel.views.common.paint.MovableObject;
import com.kyoo.pixel.views.stage.fixture.components.Component;
import com.kyoo.pixel.views.stage.fixture.components.ComponentKey;
import com.kyoo.pixel.views.stage.fixture.components.ComponentType;

import java.awt.*;
import java.util.Optional;

import static com.kyoo.pixel.utils.MoveComponentUtils.sumPoints;

public abstract class InteractiveComponent extends InteractiveComponentUnit
    implements Component, MovableObject {

  private final Component component;

  protected InteractiveComponent(Component component) {
    super(component);
    this.component = component;
  }

  @Override
  public void move(Point movement) {
    component.setPosition(sumPoints(movement, component.getPosition()));
  }

  // Proxy for Component
  @Override
  public ComponentKey getKey() {
    return component.getKey();
  }

  @Override
  public Optional<ComponentKey> getPreviousComponentKey() {
    return component.getPreviousComponentKey();
  }

  @Override
  public void setPreviousComponentKey(Optional<ComponentKey> componentKey) {
    component.setPreviousComponentKey(componentKey);
  }

  @Override
  public Optional<ComponentKey> getNextComponentKey() {
    return component.getNextComponentKey();
  }

  @Override
  public void setNextComponentKey(Optional<ComponentKey> componentKey) {
    component.setNextComponentKey(componentKey);
  }

  @Override
  public int availableInputs() {
    return component.availableInputs();
  }

  @Override
  public int availableOutputs() {
    return component.availableOutputs();
  }

  @Override
  public ComponentType type() {
    return component.type();
  }

  @Override
  public Point getEndPosition() {
    return component.getEndPosition();
  }

  public Component getComponent() {
    return component;
  }
}
