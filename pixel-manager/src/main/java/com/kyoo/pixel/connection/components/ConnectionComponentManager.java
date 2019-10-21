package com.kyoo.pixel.connection.components;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

@Getter
public final class ConnectionComponentManager {

  private Map<ComponentType, Map<Long, SelectableComponent>> components;

  public ConnectionComponentManager() {
    components = new ConcurrentHashMap<>();
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

  public Optional<SelectableComponent> getComponent(ComponentType type, long id) {
    Map<Long, SelectableComponent> componentsByType = getComponents().get(type);
    if (componentsByType != null && componentsByType.containsKey(id)) {
      return Optional.of(componentsByType.get(id));
    }
    return Optional.empty();
  }

  public Optional<SelectableComponent> lookupSelectableComponent(Point point) {
    for (Map<Long, SelectableComponent> ck : components.values()) {
      for (SelectableComponent c : ck.values()) {
        if (c.intersects(point.x, point.y)) {
          return Optional.of(c);
        }
      }
    }
    return Optional.empty();
  }

  public Optional<ConnectionComponent> lookupConnectionComponent(Point point) {
    for (Map<Long, SelectableComponent> ck : components.values()) {
      for (SelectableComponent c : ck.values()) {
        if (c instanceof ConnectionComponent && c.intersects(point.x, point.y)) {
          return Optional.of((ConnectionComponent) c);
        }
      }
    }
    return Optional.empty();
  }

}
