package com.kyoo.pixel.connection.components.impl;

import com.kyoo.pixel.connection.ConnectionProperties;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.SelectableComponent;
import java.awt.Dimension;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

public final class Led implements SelectableComponent {

  private static final Dimension LED_DIMENSION = new Dimension(1, 1);
  @Getter
  private final ComponentType componentType;
  @Getter
  @Setter
  private SelectedSide selectedSide;
  private Point position;

  public Led(Point position) {
    this.position = position;
    this.selectedSide = SelectedSide.NONE;
    this.componentType = ComponentType.LED;
  }

  @Override
  public long getId() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public SelectedSide hasSelection(int x, int y) {
    return position.x == x && position.y == y ? SelectedSide.CENTER : SelectedSide.NONE;
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
  public void move(Point movement) {

  }

  @Override
  public String description() {
    return toString();
  }

  @Override
  public void unSelect() {
    selectedSide = SelectedSide.NONE;
  }

  @Override
  public void draw(GraphicsContext gc, ConnectionProperties properties) {
    throw new UnsupportedOperationException("There is not implementation for this method");
  }

  @Override
  public String toString() {
    return position.toString();
  }
}
