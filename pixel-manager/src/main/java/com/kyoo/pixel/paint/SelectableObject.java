package com.kyoo.pixel.paint;

import java.awt.Point;

public interface SelectableObject {

  SelectedSide canSelect(Point xy);

  void select(SelectedSide selectedSide);

  SelectedSide getSelectedSide();

  void unSelect();

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
