package com.kyoo.pixel.connection.components;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

@Getter
public final class ConnectionComponentManager {

  private Map<ComponentType, Map<Long, ConnectionComponent>> components;

  public ConnectionComponentManager() {
    components = new ConcurrentHashMap<>();
  }

  public synchronized void addComponent(ConnectionComponent component) {
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

  public Optional<ConnectionComponent> getComponent(ComponentType type, long id) {
    Map<Long, ConnectionComponent> componentsByType = getComponents().get(type);
    if (componentsByType != null && componentsByType.containsKey(id)) {
      return Optional.of(componentsByType.get(id));
    }
    return Optional.empty();
  }

  public Optional<Led> lookup(ComponentType componentType, Point point) {
    switch (componentType) {
      case LED_PATH:
        if (components.containsKey(ComponentType.SQUARE_PANEL)) {
          for (ConnectionComponent panelComponent : components
              .get(ComponentType.SQUARE_PANEL).values()) {
            if (panelComponent.intersects(point.x, point.y)) {
              Optional<ConnectionComponent> internalComponent =
                  panelComponent.internalIntersects(point);
              if (internalComponent.isPresent()) {
                return Optional.of((Led) internalComponent.get());
              }
            }
          }
        }
        break;
      default:
    }
    return Optional.empty();
  }
}
