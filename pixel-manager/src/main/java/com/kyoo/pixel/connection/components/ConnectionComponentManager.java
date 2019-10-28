package com.kyoo.pixel.connection.components;

import com.google.common.collect.Lists;
import com.kyoo.pixel.connection.components.SelectableComponent.SelectedSide;
import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;

@Getter
public final class ConnectionComponentManager {

  private Map<ComponentType, Map<Long, SelectableComponent>> components;

  public ConnectionComponentManager() {
    components = new LinkedHashMap<>();
  }

  public synchronized void addComponent(SelectableComponent component) {
    if (!components.containsKey(component.getComponentType())) {
      components.put(component.getComponentType(), new LinkedHashMap<>());
    }
    if (!components.get(component.getComponentType()).containsKey(component.getId())) {
      components.get(component.getComponentType()).put(component.getId(), component);
    }
  }

  public synchronized void removeComponent(ComponentType componentType, long id) {
    if (components.containsKey(componentType) && components.get(componentType)
        .containsKey(id)) {
      components.get(componentType).remove(id);
    }
  }

  public Optional<SelectableComponent> getComponent(Long id) {
    for (Map<Long, SelectableComponent> componentsByKey : components.values()) {
      if (componentsByKey.containsKey(id)) {
        return Optional.of(componentsByKey.get(id));
      }
    }
    return Optional.empty();
  }

  public List<SelectableComponent> getComponents(List<ComponentKey> keys) {
    List<SelectableComponent> result = Lists.newArrayList();
    for (ComponentKey key : keys) {
      if (components.containsKey(key.getType())) {
        Map<Long, SelectableComponent> componentsById = components.get(key.getType());
        if (componentsById.containsKey(key.getId())) {
          result.add(componentsById.get(key.getId()));
        }
      }
    }
    return result;
  }

  public Optional<ConnectionComponent> lookupConnectionComponent(Point point) {
    for (Map<Long, SelectableComponent> ck : components.values()) {
      for (SelectableComponent c : ck.values()) {
        if (c instanceof ConnectionComponent
            && c.hasSelection(point.x, point.y) != SelectedSide.NONE) {
          return Optional.of((ConnectionComponent) c);
        }
      }
    }
    return Optional.empty();
  }

}
