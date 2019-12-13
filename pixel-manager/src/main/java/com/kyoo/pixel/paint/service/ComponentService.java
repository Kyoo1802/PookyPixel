package com.kyoo.pixel.paint.service;

import com.kyoo.pixel.paint.model.ComponentModel;

import java.awt.*;
import java.util.Collection;
import java.util.Optional;

public interface ComponentService {
  Optional<ComponentModel> getFirstComponent(double x, double y);

  Collection<ComponentModel> getComponents(Dimension selectionArea);

  void moveComponents(Collection<ComponentModel> components);

  void addComponent(ComponentModel componentModel);
}
