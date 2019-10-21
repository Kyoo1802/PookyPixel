package com.kyoo.pixel.connection.components;

import com.kyoo.pixel.connection.components.ConnectionComponent.ComponentSide;
import java.awt.Dimension;

public interface ScalableComponent extends SelectableComponent {

  void addDimension(Dimension scale);

  ComponentSide scaleIntersection(int x, int y);
}
