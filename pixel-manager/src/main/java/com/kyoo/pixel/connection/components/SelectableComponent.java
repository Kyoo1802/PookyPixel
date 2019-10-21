package com.kyoo.pixel.connection.components;

import com.kyoo.pixel.connection.components.ConnectionComponent.SelectedSide;
import java.awt.Dimension;
import java.awt.Point;

public interface SelectableComponent {

  long getId();

  SelectedSide select(int x, int y);

  Point getStartIdxPosition();

  Point getEndIdxPosition();

  Dimension getSize();

  ComponentType getComponentType();

  SelectedSide getSelectedSide();

  String description();
}
