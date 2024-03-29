package com.kyoo.pixel.views.stage.fixture.paint;

import com.kyoo.pixel.utils.draw.DrawComponentUtils;
import com.kyoo.pixel.views.common.paint.DrawableObject;
import com.kyoo.pixel.views.common.paint.SelectableObject;
import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import com.kyoo.pixel.views.stage.fixture.components.ComponentKey;
import com.kyoo.pixel.views.stage.fixture.components.ComponentType;
import com.kyoo.pixel.views.stage.fixture.components.ComponentUnit;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.Map;

public abstract class InteractiveComponentUnit
    implements ComponentUnit, DrawableObject, SelectableObject {

  private final ComponentUnit componentUnit;
  private SelectedSide selectedSide;

  protected InteractiveComponentUnit(ComponentUnit component) {
    this.componentUnit = component;
  }

  @Override
  public void draw(GraphicsContext gc, FixtureProperties properties) {
    drawComponent(gc, properties);
    if (selectedSide != SelectedSide.NONE) {
      DrawComponentUtils.drawComponentSelection(gc, properties, componentUnit);
    }
  }

  @Override
  public SelectedSide canSelect(Point xy) {
    return componentUnit.intersects(xy) ? SelectedSide.CENTER : SelectedSide.NONE;
  }

  @Override
  public void select(SelectedSide selectedSide) {
    this.selectedSide = selectedSide;
  }

  @Override
  public SelectedSide getSelectedSide() {
    return selectedSide;
  }

  @Override
  public void unSelect() {
    this.selectedSide = SelectedSide.NONE;
  }

  // Proxy for Component Unit

  @Override
  public ComponentKey getKey() {
    return componentUnit.getKey();
  }

  @Override
  public ComponentType type() {
    return componentUnit.type();
  }

  @Override
  public Point getPosition() {
    return componentUnit.getPosition();
  }

  @Override
  public void setPosition(Point point) {
    componentUnit.setPosition(point);
  }

  @Override
  public Dimension getSize() {
    return componentUnit.getSize();
  }

  @Override
  public Point getEndPosition() {
    return componentUnit.getEndPosition();
  }

  @Override
  public boolean intersects(Point point) {
    return componentUnit.intersects(point);
  }

  @Override
  public Map<String, Object> getProperties() {
    return componentUnit.getProperties();
  }

  public ComponentUnit getComponentUnit() {
    return componentUnit;
  }
}
