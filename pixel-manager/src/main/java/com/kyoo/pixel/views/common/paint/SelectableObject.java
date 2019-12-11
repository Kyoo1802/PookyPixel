package com.kyoo.pixel.views.common.paint;

import java.awt.*;

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
