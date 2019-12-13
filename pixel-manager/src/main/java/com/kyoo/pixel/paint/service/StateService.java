package com.kyoo.pixel.paint.service;

import com.kyoo.pixel.paint.model.ComponentCreationRequest;
import com.kyoo.pixel.paint.model.ComponentModel;
import com.kyoo.pixel.paint.model.SlideState;

import javax.annotation.Nonnull;

public interface StateService {
  SlideState currentState();

  void updateState(SlideState move);

  ComponentCreationRequest creationRequest();

  ComponentModel getMoveComponent();

  void setMoveComponent(@Nonnull ComponentModel componentModel);
}
