package com.kyoo.pixel.paint.service;

import com.kyoo.pixel.paint.model.ComponentCreationRequest;
import com.kyoo.pixel.paint.model.ComponentModel;

import java.util.Optional;

public interface InteractiveComponentService {
  void createComponent(double x, double y, ComponentCreationRequest creationRequest);

  void updateCreation(double x, double y);

  Optional<ComponentModel> getCreatedComponent();
}
