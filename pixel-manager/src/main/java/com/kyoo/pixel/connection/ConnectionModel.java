package com.kyoo.pixel.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.ConnectionComponentManager;
import com.kyoo.pixel.connection.components.Led;
import com.kyoo.pixel.connection.components.LedBridge;
import com.kyoo.pixel.connection.components.LedPath;
import com.kyoo.pixel.connection.components.Pointer;
import com.kyoo.pixel.connection.components.PortComponent;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Singleton
@Log4j2
public final class ConnectionModel {

  private ConnectionAction connectionAction;
  private DrawAction drawAction;
  private ConnectionComponentManager createdComponents;
  private Optional<ConnectionCommandRequest> beingCreatedComponent;
  private Optional<ConnectionComponent> selectedComponent;
  private Map<ComponentType, Integer> createdComponentsCount;
  private Pointer pointer;

  @Inject
  public ConnectionModel() {
    this.beingCreatedComponent = Optional.empty();
    this.selectedComponent = Optional.empty();
    this.createdComponentsCount = new HashMap<>();
    this.createdComponents = new ConnectionComponentManager();
    this.pointer = new Pointer(new Point(0, 0));
    this.connectionAction = ConnectionAction.NO_ACTION;
    this.drawAction = DrawAction.DRAW_SQUARE_PANEL;
  }

  public void handleMove(Point mousePosition) {
    pointer.setLocation(mousePosition);
    switch (drawAction) {
      case DRAW_PANEL_BRIDGE:

        break;
      default:
    }
  }

  public void handleAction(@Nonnull Point mousePosition) {
    Point idxPoint = PositionUtils.toIdxPosition(mousePosition);
    switch (connectionAction) {
      case NO_ACTION:
        selectComponent(idxPoint);
        break;
      case DELETE:
        deleteComponent();
        break;
      default:
        log.error("Invalid action to handle");
    }
  }

  private void selectComponent(@Nonnull Point idxPoint) {
    if (selectedComponent.isPresent() && selectedComponent.get()
        .intersects(idxPoint.x, idxPoint.y)) {
      selectedComponent.get().internalSelect(idxPoint.x, idxPoint.y);
      return;
    }
    for (Map<Long, ConnectionComponent> components : createdComponents.all().values()) {
      for (ConnectionComponent component : components.values()) {
        if (component.intersects(idxPoint.x, idxPoint.y)) {
          selectedComponent = Optional.of(component);
          return;
        }
      }
    }
    selectedComponent = Optional.empty();
  }

  private void startComponent(@Nonnull Point idxPoint) {
    switch (drawAction) {
      case DRAW_DRIVER_PORT:
        PortComponent portComponent = new PortComponent(generateId(ComponentType.PORT), idxPoint);
        createdComponents.addComponent(portComponent);
        selectedComponent = Optional.of(portComponent);
        break;
      case DRAW_LED_PATH:
        LedPath ledPath = new LedPath(generateId(ComponentType.LED_PATH), idxPoint);
        selectedComponent = Optional.of(ledPath);
        break;
      case DRAW_PANEL_BRIDGE:
        Optional<Led> ledStart = createdComponents.lookup(ComponentType.LED, idxPoint);
        if (ledStart.isPresent()) {
          LedBridge ledBridge = new LedBridge(generateId(ComponentType.PANEL_BRIDGE), idxPoint);
          selectedComponent = Optional.of(ledBridge);
        }
        break;
      default:
        log.error("Invalid case, when starting a component: " + connectionAction);
    }
  }

  private void continueComponent(Point idxPoint) {
    switch (selectedComponent.get().getConnectionType()) {
      case LED_PATH:
        LedPath ledPath = (LedPath) selectedComponent.get();
        ledPath.addLed(new Led(idxPoint, ledPath));
        break;
      default:
        log.error("Invalid case, when continue a component: " + connectionAction);
    }
  }

  private void endComponent(@Nonnull Point idxPoint) {
    switch (selectedComponent.get().getConnectionType()) {
      case SQUARE_PANEL:
        beingCreatedComponent = Optional.empty();
        break;
      case PANEL_BRIDGE:
        LedBridge ledBridge = (LedBridge) selectedComponent.get();
        ledBridge.endComponent(idxPoint);
        createdComponents.addComponent(ledBridge);
        beingCreatedComponent = Optional.empty();
        break;
      default:
        log.error("Invalid case, when ending a component: " + connectionAction);
    }
  }

  private void deleteComponent() {
    if (selectedComponent.isEmpty()) {

    }
  }

  public long generateId(ComponentType componentType) {
    if (!createdComponentsCount.containsKey(componentType)) {
      createdComponentsCount.put(componentType, 0);
    }
    int newCount = createdComponentsCount.get(componentType) + 1;
    createdComponentsCount.put(componentType, newCount);
    return newCount;
  }

  public void unSelectAction() {
    connectionAction = ConnectionAction.NO_ACTION;
    drawAction = DrawAction.UNSET;
  }

  public void selectDrawSquare(boolean select) {
    if (select) {
      connectionAction = ConnectionAction.DRAW;
      drawAction = DrawAction.DRAW_SQUARE_PANEL;
    } else {
      unSelectAction();
    }
  }

  public enum ConnectionAction {
    NO_ACTION,
    DELETE,
    DRAW,
  }

  public enum DrawAction {
    UNSET,
    DRAW_SQUARE_PANEL,
    DRAW_LED_PATH,
    DRAW_DRIVER_PORT,
    DRAW_PANEL_BRIDGE,
  }
}
