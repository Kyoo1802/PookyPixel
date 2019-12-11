package com.kyoo.pixel.views.stage.fixture.components;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public final class ComponentManager {

  private final Map<ComponentType, Integer> componentIdCounters;
  private final Map<ComponentKey, ComponentUnit> components;

  public ComponentManager() {
    this.components = new LinkedHashMap<>();
    this.componentIdCounters = new HashMap<>();
  }

  public static ComponentManager load(String path) {
    return null;
  }

  public void addComponent(ComponentUnit component) {
    components.put(component.getKey(), component);
  }

  public synchronized void removeComponent(ComponentKey componentKey) {
    components.remove(componentKey);
  }

  public Optional<ComponentUnit> getComponentUnit(ComponentKey key) {
    return Optional.ofNullable(components.get(key));
  }

  public Collection<ComponentUnit> getComponents() {
    return components.values();
  }

  public List<ComponentUnit> getComponents(Collection<ComponentKey> keys) {
    return keys.parallelStream()
        .filter(key -> components.containsKey(key))
        .map(key -> getComponentUnit(key).get())
        .collect(Collectors.toList());
  }

  public Optional<ComponentUnit> lookupComponentUnit(Point point) {
    return components
        .entrySet()
        .parallelStream()
        .filter(entry -> entry.getValue().intersects(point))
        .map(entry -> getComponentUnit(entry.getKey()).get())
        .findFirst();
  }

  public Optional<Component> lookupComponent(Point point) {
    return components
        .entrySet()
        .parallelStream()
        .map(entry -> getComponent(entry.getKey()))
        .filter(c -> c.isPresent())
        .filter(c -> c.get().intersects(point))
        .map(c -> c.get())
        .findFirst();
  }

  public Optional<Component> getComponent(ComponentKey key) {
    if (components.containsKey(key)) {
      ComponentUnit unit = components.get(key);
      if (unit instanceof Component) {
        return Optional.ofNullable((Component) unit);
      }
    }
    return Optional.empty();
  }

  public ComponentKey generateKey(ComponentType componentType) {
    if (!componentIdCounters.containsKey(componentType)) {
      componentIdCounters.put(componentType, 0);
    }
    int newCount = componentIdCounters.get(componentType) + 1;
    componentIdCounters.put(componentType, newCount);
    return ComponentKey.from(newCount, componentType);
  }

  public void save(String path) {}
}
