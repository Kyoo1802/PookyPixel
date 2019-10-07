package com.kyoo.pixel.views.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.data.connection.ComponentType;
import com.kyoo.pixel.data.connection.ConnectionComponent;
import com.kyoo.pixel.data.connection.ConnectionComponentSource;
import com.kyoo.pixel.data.connection.CreationType;
import com.kyoo.pixel.data.connection.Led;
import com.kyoo.pixel.data.connection.LedBridge;
import com.kyoo.pixel.data.connection.LedPath;
import com.kyoo.pixel.data.connection.Pointer;
import com.kyoo.pixel.data.connection.PortComponent;
import com.kyoo.pixel.data.connection.SquarePanel;
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
  private ConnectionComponentSource createdComponents;
  private Optional<ConnectionComponent> beingCreatedComponent;
  private Optional<ConnectionComponent> selectedComponent;
  private Map<ComponentType, Integer> createdComponentsCount;
  private Pointer pointer;

  @Inject
  public ConnectionModel() {
    this.beingCreatedComponent = Optional.empty();
    this.selectedComponent = Optional.empty();
    this.createdComponentsCount = new HashMap<>();
    this.createdComponents = new ConnectionComponentSource();
    this.pointer = new Pointer(new Point(0, 0));
    this.connectionAction = ConnectionAction.DRAW;
    this.drawAction = DrawAction.DRAW_SQUARE_PANEL;
  }

  public void handleMove(Point actionPosition) {
    pointer.setLocation(actionPosition);
    switch (drawAction){
      case DRAW_PANEL_BRIDGE:

        break;
      default:
    }
  }

  public void handleAction(@Nonnull Point mousePoint) {
    switch (connectionAction) {
      case NO_ACTION:
        selectComponent(mousePoint);
      case DELETE:
        deleteComponent();
      case DRAW:
        if (beingCreatedComponent.isEmpty()) {
          startComponent(mousePoint);
        } else if (beingCreatedComponent.get().creationType() == CreationType.MULTI_POINT) {
          continueComponent(mousePoint);
        } else {
          endComponent(mousePoint);
        }
    }
  }

  private boolean selectComponent(Point selectPoint) {
    if (selectedComponent.isPresent() && selectedComponent.get()
        .intersects(selectPoint.x, selectPoint.y)) {
      return selectedComponent.get().internalSelect(selectPoint.x, selectPoint.y);
    }
    for (ConnectionComponent component : createdComponents.all()) {
      if (component.intersects(selectPoint.x, selectPoint.y)) {
        selectedComponent = Optional.of(component);
        return true;
      }
    }
    selectedComponent = Optional.empty();
    return false;
  }

  private void startComponent(@Nonnull Point mousePoint) {
    switch (drawAction) {
      case DRAW_DRIVER_PORT:
        PortComponent portComponent = new PortComponent(countFor(ComponentType.PORT), mousePoint);
        createdComponents.addComponent(portComponent);
        selectedComponent = Optional.of(portComponent);
        break;
      case DRAW_LED_PATH:
        LedPath ledPath = new LedPath(countFor(ComponentType.LED_PATH), mousePoint);
        beingCreatedComponent = Optional.of(ledPath);
        selectedComponent = Optional.of(ledPath);
        break;
      case DRAW_SQUARE_PANEL:
        SquarePanel squarePanel = new SquarePanel(countFor(ComponentType.SQUARE_PANEL), mousePoint);
        beingCreatedComponent = Optional.of(squarePanel);
        selectedComponent = Optional.of(squarePanel);
        break;
      case DRAW_PANEL_BRIDGE:
        Optional<Led> ledStart = createdComponents.lookup(ComponentType.LED, mousePoint);
        if (ledStart.isPresent()) {
          LedBridge ledBridge = new LedBridge(countFor(ComponentType.PANEL_BRIDGE), mousePoint);
          beingCreatedComponent = Optional.of(ledBridge);
          selectedComponent = Optional.of(ledBridge);
        }
        break;
      default:
        log.error("Invalid case, when starting a component: " + connectionAction);
    }
  }

  private void continueComponent(Point mousePoint) {
    switch (selectedComponent.get().connectionType()) {
      case LED_PATH:
        LedPath ledPath = (LedPath) selectedComponent.get();
        ledPath.addLed(new Led(mousePoint, ledPath));
        break;
      default:
        log.error("Invalid case, when starting a component: " + connectionAction);
    }
  }

  private void endComponent(@Nonnull Point mousePoint) {
    switch (selectedComponent.get().connectionType()) {
      case SQUARE_PANEL:
        SquarePanel squarePanel = (SquarePanel) selectedComponent.get();
        squarePanel.endComponent(mousePoint);
        createdComponents.addComponent(squarePanel);
        beingCreatedComponent = Optional.empty();
        break;
      case PANEL_BRIDGE:
        LedBridge ledBridge = (LedBridge) selectedComponent.get();
        ledBridge.endComponent(mousePoint);
        createdComponents.addComponent(ledBridge);
        beingCreatedComponent = Optional.empty();
        break;
      default:
        log.error("Invalid case, when starting a component: " + connectionAction);
    }
  }

  private void deleteComponent() {
    if (selectedComponent.isEmpty()) {

    }
  }

  private int countFor(ComponentType componentType) {
    if (!createdComponentsCount.containsKey(componentType)) {
      createdComponentsCount.put(componentType, 0);
    }
    int newCount = createdComponentsCount.get(componentType) + 1;
    createdComponentsCount.put(componentType, newCount);
    return newCount;
  }

  public void selectAction(ConnectionAction connectionAction) {
    this.connectionAction=connectionAction;
  }

  public void selectDraw(DrawAction drawAction) {
    this.drawAction = drawAction;
  }

  enum ConnectionAction {
    NO_ACTION,
    DELETE,
    DRAW,
  }
  enum DrawAction {
    UNSET,
    DRAW_SQUARE_PANEL,
    DRAW_LED_PATH,
    DRAW_DRIVER_PORT,
    DRAW_PANEL_BRIDGE,
  }
}
