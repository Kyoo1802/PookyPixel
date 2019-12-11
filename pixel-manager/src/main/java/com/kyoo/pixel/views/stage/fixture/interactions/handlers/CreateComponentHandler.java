package com.kyoo.pixel.views.stage.fixture.interactions.handlers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.views.stage.fixture.FixtureCommandManager;
import com.kyoo.pixel.views.stage.fixture.FixtureModel;
import com.kyoo.pixel.views.stage.fixture.commands.CreateBridgeCommand;
import com.kyoo.pixel.views.stage.fixture.commands.CreateBridgeCommand.CreateBridgeCommandRequest;
import com.kyoo.pixel.views.stage.fixture.commands.CreateLedPathCommand;
import com.kyoo.pixel.views.stage.fixture.commands.CreateLedPathCommand.CreateLedPathCommandRequest;
import com.kyoo.pixel.views.stage.fixture.commands.CreateSquarePanelCommand;
import com.kyoo.pixel.views.stage.fixture.commands.CreateSquarePanelCommand.CreateSquarePanelCommandRequest;
import com.kyoo.pixel.views.stage.fixture.components.Component;
import com.kyoo.pixel.views.stage.fixture.components.ComponentType;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.util.Optional;

@Log4j2
@Singleton
public final class CreateComponentHandler {

  private FixtureModel model;
  private FixtureCommandManager commandManager;

  @Inject
  public CreateComponentHandler(FixtureModel model, FixtureCommandManager commandManager) {
    this.model = model;
    this.commandManager = commandManager;
  }

  public void createSquarePanel() {
    if (model.getActiveCommandRequest().isEmpty()) {
      CreateSquarePanelCommandRequest request =
          CreateSquarePanelCommandRequest.builder()
              .id(model.generateCommandId())
              .componentKey(
                  model
                      .getInteractiveComponentManager()
                      .getComponentManager()
                      .generateKey(ComponentType.SQUARE_PANEL))
              .startIdxPosition(model.getPointer().idxPositionCopy())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      CreateSquarePanelCommandRequest request =
          ((CreateSquarePanelCommandRequest) model.getActiveCommandRequest().get())
              .toBuilder()
              .endIdxPosition(model.getPointer().idxPositionCopy())
              .build();
      commandManager.execute(new CreateSquarePanelCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }

  public void createLedPath(boolean hasFinished) {
    Point pointerPosition = model.getPointer().idxPositionCopy();

    if (model.getActiveCommandRequest().isEmpty()) {
      CreateLedPathCommandRequest request =
          CreateLedPathCommandRequest.builder()
              .id(model.generateCommandId())
              .componentKey(
                  model
                      .getInteractiveComponentManager()
                      .getComponentManager()
                      .generateKey(ComponentType.LED_PATH))
              .idxPositions(Sets.newLinkedHashSet(Lists.newArrayList(pointerPosition)))
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else if (!hasFinished) {
      CreateLedPathCommandRequest request =
          (CreateLedPathCommandRequest) model.getActiveCommandRequest().get();
      Optional<Point> ledInSamePosition =
          request
              .getIdxPositions()
              .parallelStream()
              .filter(p -> p.equals(pointerPosition))
              .findAny();
      if (ledInSamePosition.isEmpty()) {
        request.getIdxPositions().add(pointerPosition);
      }
    } else {
      CreateLedPathCommandRequest request =
          (CreateLedPathCommandRequest) model.getActiveCommandRequest().get();
      commandManager.execute(new CreateLedPathCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }

  public void createBridgePort() {
    Optional<Component> component =
        model
            .getInteractiveComponentManager()
            .getComponentManager()
            .lookupComponent(model.getPointer().getIdxPosition());
    if (component.isEmpty()) {
      return;
    }

    if (model.getActiveCommandRequest().isEmpty()) { // First Interaction
      if (component.get().availableOutputs() == 0) {
        return;
      }
      CreateBridgeCommandRequest request =
          CreateBridgeCommandRequest.builder()
              .id(model.generateCommandId())
              .componentKey(
                  model
                      .getInteractiveComponentManager()
                      .getComponentManager()
                      .generateKey(ComponentType.LED_PATH))
              .startComponent(component.get())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      CreateBridgeCommandRequest request =
          ((CreateBridgeCommandRequest) model.getActiveCommandRequest().get());
      // Avoid adding end bridge to a Led component which already contains an end bridge, and also
      // avoid adding a bridge to the component itself.
      if (component.get().availableInputs() == 0
          || detectPath(request.getStartComponent(), component.get())) {
        return;
      }
      request = request.toBuilder().endComponent(component.get()).build();
      commandManager.execute(new CreateBridgeCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }

  private boolean detectPath(Component startComponent, Component endComponent) {
    Component currentComponent = startComponent;
    while (currentComponent.getNextComponentKey().isPresent()) {
      Optional<Component> nextComponent =
          model
              .getInteractiveComponentManager()
              .getComponentManager()
              .getComponent(currentComponent.getNextComponentKey().get());
      if (nextComponent.isEmpty()) {
        break;
      } else {
        currentComponent = nextComponent.get();
      }
    }
    return currentComponent == endComponent;
  }

  public void cancel() {
    model.setActiveCommandRequest(Optional.empty());
  }
}
