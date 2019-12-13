package com.kyoo.pixel.paint.service;

import com.kyoo.pixel.paint.model.ComponentModel;

import java.util.Collection;

public interface SelectionService {

  void select(ComponentModel componentModel);

  void selectAll(Collection<ComponentModel> selectedComponents);

  Collection<ComponentModel> getSelectedComponents();
}
