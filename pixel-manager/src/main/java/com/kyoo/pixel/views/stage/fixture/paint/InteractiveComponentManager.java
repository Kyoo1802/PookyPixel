package com.kyoo.pixel.views.stage.fixture.paint;

import com.kyoo.pixel.views.common.paint.ScalableObject;
import com.kyoo.pixel.views.common.paint.SelectableObject.SelectedSide;
import com.kyoo.pixel.views.stage.fixture.components.ComponentKey;
import com.kyoo.pixel.views.stage.fixture.components.ComponentManager;
import com.kyoo.pixel.views.stage.fixture.components.ComponentUnit;
import lombok.Getter;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class InteractiveComponentManager {

  @Getter private final ComponentManager componentManager;

  public InteractiveComponentManager() {
    this(new ComponentManager());
  }

  public InteractiveComponentManager(ComponentManager componentManager) {
    this.componentManager = componentManager;
  }

  public void select(ComponentKey key) {
    select(key, SelectedSide.CENTER);
  }

  public void select(ComponentKey key, SelectedSide selectionSide) {
    Optional<InteractiveComponentUnit> component = getInteractiveComponentUnit(key);
    if (component.isPresent()) {
      component.get().select(selectionSide);
    }
  }

  public void toggleSelect(ComponentKey key) {
    Optional<InteractiveComponentUnit> component = getInteractiveComponentUnit(key);
    if (component.isPresent()) {
      if (component.get().getSelectedSide() == SelectedSide.NONE) {
        component.get().select(SelectedSide.CENTER);
      } else {
        component.get().unSelect();
      }
    }
  }

  public Optional<InteractiveComponentUnit> getInteractiveComponentUnit(ComponentKey key) {
    Optional<ComponentUnit> component = getComponentManager().getComponentUnit(key);
    if (component.isPresent() && component.get() instanceof InteractiveComponentUnit) {
      return Optional.of((InteractiveComponent) component.get());
    }
    return Optional.empty();
  }

  public List<InteractiveComponent> selectedComponents() {
    return getComponentManager()
        .getComponents()
        .parallelStream()
        .map(component -> getInteractiveComponent(component.getKey()))
        .filter(iComponent -> iComponent.isPresent())
        .filter(iComponent -> iComponent.get().getSelectedSide() != SelectedSide.NONE)
        .map(iComponent -> iComponent.get())
        .collect(Collectors.toList());
  }

  public Optional<InteractiveComponent> getInteractiveComponent(ComponentKey key) {
    Optional<ComponentUnit> component = getComponentManager().getComponentUnit(key);
    if (component.isPresent() && component.get() instanceof InteractiveComponent) {
      return Optional.of((InteractiveComponent) component.get());
    }
    return Optional.empty();
  }

  public void clearSelection() {
    Object s = selectedComponents();
    selectedComponents().forEach(iComponent -> iComponent.unSelect());
  }

  public void moveSelectedComponents(Point movement) {
    selectedComponents().forEach(iComponent -> iComponent.move(movement));
  }

  public Collection<InteractiveComponentUnit> getInteractiveComponents() {
    return getComponentManager()
        .getComponents()
        .parallelStream()
        .map(component -> getInteractiveComponentUnit(component.getKey()))
        .filter(iComponent -> iComponent.isPresent())
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  public Optional<ScalableObject> getScalableComponent(ComponentKey key) {
    Optional<InteractiveComponent> componentUnit = getInteractiveComponent(key);
    if (componentUnit.isPresent() && componentUnit.get() instanceof InteractiveSquarePanel) {
      return Optional.of((ScalableObject) componentUnit.get());
    }
    return Optional.empty();
  }

  public boolean scale(ComponentKey key, Dimension scale) {
    Optional<ScalableObject> component = getScalableComponent(key);
    if (component.isEmpty()) {
      return component.get().scale(scale);
    }
    return false;
  }

  public List<ComponentKey> selectedComponentKeys() {
    return selectedComponents().parallelStream().map(c -> c.getKey()).collect(Collectors.toList());
  }

  public List<InteractiveComponent> getInteractiveComponent(List<ComponentKey> keysToMove) {
    return keysToMove
        .parallelStream()
        .map(k -> getInteractiveComponent(k))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }
}
