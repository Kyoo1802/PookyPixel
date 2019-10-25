package com.kyoo.pixel.connection.components;

import com.kyoo.pixel.connection.ConnectionProperties;
import java.awt.Dimension;
import java.awt.Point;
import javafx.scene.canvas.GraphicsContext;

public interface SelectableComponent {

  long getId();

  SelectedSide hasSelection(int x, int y);

  void setSelectedSide(SelectedSide selectedSide);

  Point getStartIdxPosition();

  Point getEndIdxPosition();

  Dimension getSize();

  ComponentType getComponentType();

  SelectedSide getSelectedSide();

  void move(Point movement);

  String description();

  void unSelect();

  void draw(GraphicsContext gc, ConnectionProperties properties);

  enum SelectedSide {
    NONE,
    CENTER,
    UPPER,
    UPPER_LEFT,
    LEFT,
    UPPER_RIGHT,
    RIGHT,
    BOTTOM_LEFT,
    BOTTOM,
    BOTTOM_RIGHT
  }
}
