package com.kyoo.pixel.connection.components.impl;

import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.ConnectionComponent.SelectedSide;
import com.kyoo.pixel.connection.components.SelectableComponent;
import java.awt.Dimension;
import java.awt.Point;
import lombok.Getter;

public final class Led implements SelectableComponent {

  private static final Dimension LED_DIMENSION = new Dimension(1, 1);
  @Getter
  private final ComponentType componentType;
  @Getter
  private SelectedSide selectedSide;
  private Point position;

  public Led(Point position) {
    this(position, null);
  }

  public Led(Point position, ConnectionComponent parentComponent) {
    this.position = position;
    this.selectedSide = SelectedSide.NONE;
    this.componentType = ComponentType.LED;
  }

  @Override
  public long getId() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public SelectedSide select(int x, int y) {
    return position.x == x && position.y == y ? setSelectedSide(SelectedSide.CENTER) :
        setSelectedSide(SelectedSide.NONE);
  }

  private SelectedSide setSelectedSide(SelectedSide selectedSide) {
    return this.selectedSide = selectedSide;
  }

  @Override
  public Point getStartIdxPosition() {
    return position;
  }

  @Override
  public Point getEndIdxPosition() {
    return position;
  }

  @Override
  public Dimension getSize() {
    return LED_DIMENSION;
  }

  @Override
  public String description() {
    return toString();
  }

  @Override
  public String toString() {
    return position.toString();
  }
}
