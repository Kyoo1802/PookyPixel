package com.kyoo.pixel.paint.service;

import com.kyoo.pixel.paint.model.ComponentModel;
import com.kyoo.pixel.paint.model.InteractionState;

import java.awt.*;

public interface InteractionService {
  void createSelectionRect(double x, double y);

  InteractionState interactionState();

  void updateSelection(double x, double y);

  Dimension getSelection();

  void createMoveRect(ComponentModel moveComponent);

  void updateMove(double x, double y);
}
