package com.kyoo.pixel.data.connection;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ConnectionComponentSource {

  private List<ConnectionComponent> allComponents;
  private Map<ComponentType, List<ConnectionComponent>> componentsView;

  public ConnectionComponentSource() {
    allComponents = new CopyOnWriteArrayList<>();
    componentsView = new HashMap<>();
  }

  public synchronized void addComponent(ConnectionComponent component) {
    allComponents.add(component);
    if (!componentsView.containsKey(component.connectionType())) {
      componentsView.put(component.connectionType(), new LinkedList<>());
    }
    componentsView.get(component.connectionType()).add(component);
  }

  public synchronized void removeComponent(ConnectionComponent component) {
    allComponents.remove(component);
    if (componentsView.containsKey(component.connectionType())) {
      componentsView.get(component.connectionType()).remove(component);
    }
  }

  public synchronized List<ConnectionComponent> all() {
    return allComponents;
  }

  public Optional<Led> lookup(ComponentType componentType, Point point) {
    switch (componentType) {
      case LED_PATH:
        if (componentsView.containsKey(ComponentType.SQUARE_PANEL)) {
          for (ConnectionComponent panelComponent : componentsView
              .get(ComponentType.SQUARE_PANEL)) {
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
