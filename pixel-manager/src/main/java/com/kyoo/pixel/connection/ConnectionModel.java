package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponentManager;
import com.kyoo.pixel.connection.components.Pointer;
import com.kyoo.pixel.connection.components.SelectableComponent;
import com.kyoo.pixel.connection.components.SelectableComponent.SelectedSide;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest;
import com.kyoo.pixel.connection.components.impl.pointers.DefaultPointer;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Singleton
@Log4j2
public final class ConnectionModel {

  private final ConnectionComponentManager createdComponentsManager;
  private final LinkedHashMap<Long, SelectableComponent> selectedComponents;
  private final Map<ComponentType, Integer> createdComponentsCount;
  private final Pointer pointer;
  private final Dimension dimension;

  private ConnectionState connectionState;
  private Optional<ConnectionCommandRequest> activeCommandRequest;
  private TransformationAction transformationAction;

  @Inject
  public ConnectionModel() {
    this.activeCommandRequest = Optional.empty();
    this.selectedComponents = new LinkedHashMap<>();
    this.createdComponentsCount = new HashMap<>();
    this.createdComponentsManager = new ConnectionComponentManager();
    this.pointer = new DefaultPointer();
    this.connectionState = ConnectionState.NO_ACTION;
    this.transformationAction = TransformationAction.UNSET;
    this.dimension = new Dimension(400, 400);
  }

  public void handlePointerMovement(Point canvasPointerPosition) {
    pointer.setCanvasPosition(canvasPointerPosition);
  }

  public void addComponent(SelectableComponent component) {
    createdComponentsManager.addComponent(component);
    clearSelection();
    select(component, SelectedSide.CENTER);
  }

  public void removeComponent(ComponentType componentType, long id) {
    createdComponentsManager.removeComponent(componentType, id);
  }

  public long generateId(ComponentType componentType) {
    if (!createdComponentsCount.containsKey(componentType)) {
      createdComponentsCount.put(componentType, 0);
    }
    int newCount = createdComponentsCount.get(componentType) + 1;
    createdComponentsCount.put(componentType, newCount);
    return newCount;
  }

  public void setActiveCommandRequest(Optional<ConnectionCommandRequest> activeCommandRequest) {
    this.activeCommandRequest = activeCommandRequest;
  }

  public boolean hasActiveCommandRequest() {
    return getActiveCommandRequest().isPresent();
  }

  public void setConnectionState(ConnectionState connectionState, boolean value) {
    if (value) {
      this.connectionState = connectionState;
    } else {
      this.connectionState = ConnectionState.NO_ACTION;
    }
  }

  public Pointer getPointer() {
    return pointer;
  }

  public void select(SelectableComponent component, SelectedSide selectionSide) {
    component.setSelectedSide(selectionSide);
    getSelectedComponents().put(component.getId(), component);
  }

  public void unSelect(SelectableComponent component) {
    selectedComponents.remove(component.getId());
    component.unSelect();
  }

  public void clearSelection() {
    selectedComponents.values().parallelStream().forEach(c->c.unSelect());
    selectedComponents.clear();
  }

  public enum ConnectionState {
    NO_ACTION,
    CREATE_SQUARE_PANEL,
    CREATE_LED_PATH,
    CREATE_DRIVER_PORT,
    CREATE_LED_BRIDGE,
  }

  public enum TransformationAction {
    UNSET,
    MOVE,
    SCALE,
  }
}
