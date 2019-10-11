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

  private ConnectionAction connectionActionState;
  private DrawAction drawActionState;
  private ConnectionComponentManager createdComponentsManager;
  private Optional<ConnectionCommandRequest> beingCreatedComponent;
  private Optional<ConnectionComponent> selectedComponent;
  private Map<ComponentType, Integer> createdComponentsCount;
  private Pointer pointer;

  @Inject
  public ConnectionModel() {
    this.beingCreatedComponent = Optional.empty();
    this.selectedComponent = Optional.empty();
    this.createdComponentsCount = new HashMap<>();
    this.createdComponentsManager = new ConnectionComponentManager();
    this.pointer = new Pointer(new Point(0, 0));
    this.connectionActionState = ConnectionAction.NO_ACTION;
    this.drawActionState = DrawAction.DRAW_SQUARE_PANEL;
  }

  public void handleMove(Point mousePosition) {
    pointer.setLocation(mousePosition);
    switch (drawActionState) {
      case DRAW_PANEL_BRIDGE:

        break;
      default:
    }
  }

  private void startComponent(@Nonnull Point idxPoint) {
    switch (drawActionState) {
      case DRAW_DRIVER_PORT:
        PortComponent portComponent = new PortComponent(generateId(ComponentType.PORT), idxPoint);
        createdComponentsManager.addComponent(portComponent);
        selectedComponent = Optional.of(portComponent);
        break;
      case DRAW_LED_PATH:
        LedPath ledPath = new LedPath(generateId(ComponentType.LED_PATH), idxPoint);
        selectedComponent = Optional.of(ledPath);
        break;
      case DRAW_PANEL_BRIDGE:
        Optional<Led> ledStart = createdComponentsManager.lookup(ComponentType.LED, idxPoint);
        if (ledStart.isPresent()) {
          LedBridge ledBridge = new LedBridge(generateId(ComponentType.PANEL_BRIDGE), idxPoint);
          selectedComponent = Optional.of(ledBridge);
        }
        break;
      default:
        log.error("Invalid case, when starting a component: " + connectionActionState);
    }
  }

  private void continueComponent(Point idxPoint) {
    switch (selectedComponent.get().getConnectionType()) {
      case LED_PATH:
        LedPath ledPath = (LedPath) selectedComponent.get();
        ledPath.addLed(new Led(idxPoint, ledPath));
        break;
      default:
        log.error("Invalid case, when continue a component: " + connectionActionState);
    }
  }

  private void endComponent(@Nonnull Point idxPoint) {
    switch (selectedComponent.get().getConnectionType()) {
      case PANEL_BRIDGE:
        LedBridge ledBridge = (LedBridge) selectedComponent.get();
        ledBridge.endComponent(idxPoint);
        createdComponentsManager.addComponent(ledBridge);
        beingCreatedComponent = Optional.empty();
        break;
      default:
        log.error("Invalid case, when ending a component: " + connectionActionState);
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

  public void unSelectActionState() {
    connectionActionState = ConnectionAction.NO_ACTION;
    drawActionState = DrawAction.UNSET;
  }

  public void selectDrawSquareState(boolean select) {
    if (select) {
      connectionActionState = ConnectionAction.DRAW;
      drawActionState = DrawAction.DRAW_SQUARE_PANEL;
    } else {
      unSelectActionState();
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
