package com.kyoo.pixel.connection.components;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ConnectionComponentManager {

  private Map<ComponentType, Map<Long, ConnectionComponent>> allComponents;

  public ConnectionComponentManager() {
    allComponents = new ConcurrentHashMap<>();
  }

  public synchronized void addComponent(ConnectionComponent component) {
    if (!allComponents.containsKey(component.getComponentType())) {
      allComponents.put(component.getComponentType(), new LinkedHashMap<>());
    }
    if (!allComponents.get(component.getComponentType()).containsKey(component.getId())) {
      allComponents.get(component.getComponentType()).put(component.getId(), component);
    }
  }

  public synchronized void removeComponent(ComponentType componentType, long id) {
    if (allComponents.containsKey(componentType) && allComponents.get(componentType)
        .containsKey(id)) {
      allComponents.get(componentType).remove(id);
    }
  }

  public synchronized Map<ComponentType, Map<Long, ConnectionComponent>> all() {
    return allComponents;
  }

  public Optional<Led> lookup(ComponentType componentType, Point point) {
    switch (componentType) {
      case LED_PATH:
        if (allComponents.containsKey(ComponentType.SQUARE_PANEL)) {
          for (ConnectionComponent panelComponent : allComponents
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
