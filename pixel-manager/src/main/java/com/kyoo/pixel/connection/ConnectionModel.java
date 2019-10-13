package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.ConnectionComponentManager;
import com.kyoo.pixel.connection.components.Pointer;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Singleton
@Log4j2
public final class ConnectionModel {

  private ConnectionAction connectionActionState;
  private TransformationAction transformationActionState;
  private ConnectionComponentManager createdComponentsManager;
  private Optional<ConnectionCommandRequest> beingCreatedComponent;
  private Optional<ConnectionComponent> selectedComponent;
  private Map<ComponentType, Integer> createdComponentsCount;
  private Pointer idxPointer;

  @Inject
  public ConnectionModel() {
    this.beingCreatedComponent = Optional.empty();
    this.selectedComponent = Optional.empty();
    this.createdComponentsCount = new HashMap<>();
    this.createdComponentsManager = new ConnectionComponentManager();
    this.idxPointer = new Pointer(new Point(0, 0));
    this.connectionActionState = ConnectionAction.NO_ACTION;
    this.transformationActionState = TransformationAction.MOVE;
  }

  public void handlePointerMovement(Point canvasPointerPosition) {
    idxPointer.setLocation(PositionUtils.toIdxPosition(canvasPointerPosition));
  }

  public void addComponent(ConnectionComponent component) {
    createdComponentsManager.addComponent(component);
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

  public void unSelectActionState() {
    connectionActionState = ConnectionAction.NO_ACTION;
  }

  public void selectDrawSquarePanelState(boolean select) {
    beingCreatedComponent = Optional.empty();
    if (select) {
      connectionActionState = ConnectionAction.DRAW_SQUARE_PANEL;
    } else {
      unSelectActionState();
    }
  }

  public void selectDrawDriverPortState(boolean select) {
    beingCreatedComponent = Optional.empty();
    if (select) {
      connectionActionState = ConnectionAction.DRAW_DRIVER_PORT;
    } else {
      unSelectActionState();
    }
  }

  public boolean thereIsNotComponentBeingCreated() {
    return getBeingCreatedComponent().isEmpty();
  }

  public enum MouseState {
    MOVED,
    CLICKED,
    DRAGGED,
    PRESSED,
    RELEASED
  }

  public enum ConnectionAction {
    NO_ACTION,
    DRAW_SQUARE_PANEL,
    DRAW_LED_PATH,
    DRAW_DRIVER_PORT,
    DRAW_PANEL_BRIDGE,
  }


  public enum TransformationAction {
    UNSET,
    MOVE,
    SCALE,
    ROTATE,
  }
}
