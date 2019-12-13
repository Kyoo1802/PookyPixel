package com.kyoo.pixel.views.stage.fixture;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.views.stage.fixture.commands.FixtureCommandRequest;
import com.kyoo.pixel.views.stage.fixture.components.ComponentKey;
import com.kyoo.pixel.views.stage.fixture.paint.InteractiveBridge;
import com.kyoo.pixel.views.stage.fixture.paint.InteractiveComponent;
import com.kyoo.pixel.views.stage.fixture.paint.InteractiveComponentManager;
import com.kyoo.pixel.views.stage.fixture.paint.InteractiveComponentUnit;
import com.kyoo.pixel.views.stage.fixture.pointers.DefaultPointer;
import com.kyoo.pixel.views.stage.fixture.pointers.Pointer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Getter
@Singleton
@Log4j2
public final class FixtureModel {

  private final InteractiveComponentManager interactiveComponentManager;
  private final Pointer pointer;
  private final Dimension dimension;
  @Setter private Optional<FixtureCommandRequest> activeCommandRequest;
  @Setter private FixtureAction stateAction;
  @Setter private int commandId;

  @Inject
  public FixtureModel() {
    this.activeCommandRequest = Optional.empty();
    this.pointer = new DefaultPointer();
    this.stateAction = FixtureAction.NO_ACTION;
    this.interactiveComponentManager = new InteractiveComponentManager();
    this.dimension = new Dimension(400, 400);
    this.commandId = 1;
  }

  public void movePointer(Point canvasPointerPosition) {
    pointer.setCanvasPosition(canvasPointerPosition);
  }

  public void addComponent(InteractiveComponentUnit component) {
    getInteractiveComponentManager().clearSelection();
    getInteractiveComponentManager().getComponentManager().addComponent(component);
    getInteractiveComponentManager().select(component.getKey());
  }

  public void removeComponent(ComponentKey key) {
    getInteractiveComponentManager().getComponentManager().removeComponent(key);
  }

  public void singleSelectComponent(ComponentKey key) {
    clearSelectedComponents();
    getInteractiveComponentManager().select(key);
  }

  public void multiSelectComponent(ComponentKey key) {
    getInteractiveComponentManager().toggleSelect(key);
  }

  public void joinComponents(InteractiveBridge bridge) {
    Optional<InteractiveComponent> componentA =
        getInteractiveComponentManager()
            .getInteractiveComponent(bridge.getComponentUnit().getStartComponentKey());
    Optional<InteractiveComponent> componentB =
        getInteractiveComponentManager()
            .getInteractiveComponent(bridge.getComponentUnit().getEndComponentKey());
    if (componentA.isPresent() && componentB.isPresent()) {
      componentA
          .get()
          .setNextComponentKey(Optional.of(bridge.getComponentUnit().getEndComponentKey()));
      componentB
          .get()
          .setPreviousComponentKey(Optional.of(bridge.getComponentUnit().getStartComponentKey()));
      addComponent(bridge);
    }
  }

  public void separateComponents(InteractiveBridge bridge) {
    Optional<InteractiveComponent> componentA =
        getInteractiveComponentManager()
            .getInteractiveComponent(bridge.getComponentUnit().getStartComponentKey());
    Optional<InteractiveComponent> componentB =
        getInteractiveComponentManager()
            .getInteractiveComponent(bridge.getComponentUnit().getEndComponentKey());
    if (componentA.isPresent() && componentB.isPresent()) {
      componentA.get().setNextComponentKey(Optional.empty());
      componentB.get().setPreviousComponentKey(Optional.empty());
      removeComponent(bridge.getKey());
    }
  }

  public void clearSelectedComponents() {
    getInteractiveComponentManager().clearSelection();
  }

  public long generateCommandId() {
    return commandId++;
  }

  public List<ComponentKey> selectedComponentKeys() {
    return interactiveComponentManager.selectedComponentKeys();
  }

  public boolean canMove(Point point) {
    return getInteractiveComponentManager()
        .selectedComponents()
        .parallelStream()
        .filter(c -> c.intersects(point))
        .findAny()
        .isPresent();
  }

  public boolean canScale(Point canvasPosition) {
    // can scale just if one element is selected and the element is scalable
    return false;
  }

  public void moveComponents(List<ComponentKey> keysToMove, Point movement) {
    getInteractiveComponentManager()
        .getInteractiveComponent(keysToMove)
        .forEach(iC -> iC.move(movement));
  }

  public enum FixtureAction {
    NO_ACTION,
    CREATE_SQUARE_PANEL,
    CREATE_LED_PATH,
    CREATE_LED_BRIDGE,
  }
}
