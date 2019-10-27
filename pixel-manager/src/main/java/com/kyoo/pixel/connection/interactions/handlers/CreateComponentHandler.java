package com.kyoo.pixel.connection.interactions.handlers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandManager;
import com.kyoo.pixel.connection.components.commands.CreateBridgeCommand;
import com.kyoo.pixel.connection.components.commands.CreateBridgeCommand.CreateBridgeCommandRequest;
import com.kyoo.pixel.connection.components.commands.CreateDriverPortCommand;
import com.kyoo.pixel.connection.components.commands.CreateDriverPortCommand.CreateDriverPortRequest;
import com.kyoo.pixel.connection.components.commands.CreateLedPathCommand;
import com.kyoo.pixel.connection.components.commands.CreateLedPathCommand.CreateLedPathCommandRequest;
import com.kyoo.pixel.connection.components.commands.CreateSquarePanelCommand;
import com.kyoo.pixel.connection.components.commands.CreateSquarePanelCommand.CreateSquarePanelCommandRequest;
import java.awt.Point;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class CreateComponentHandler {

  private ConnectionModel model;
  private ConnectionCommandManager commandManager;

  @Inject
  public CreateComponentHandler(ConnectionModel model, ConnectionCommandManager commandManager) {
    this.model = model;
    this.commandManager = commandManager;
  }

  public void createSquarePanel() {
    if (!model.hasActiveCommandRequest()) {
      CreateSquarePanelCommandRequest request =
          CreateSquarePanelCommandRequest.builder()
              .id(model.generateId(ComponentType.SQUARE_PANEL))
              .commandType(ComponentType.SQUARE_PANEL)
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

  public void createDriverPort() {
    CreateDriverPortRequest request =
        CreateDriverPortRequest.builder()
            .id(model.generateId(ComponentType.DRIVER_PORT))
            .commandType(ComponentType.DRIVER_PORT)
            .idxPosition(model.getPointer().idxPositionCopy())
            .build();
    commandManager.execute(new CreateDriverPortCommand(model, request));
  }

  public void createLedPath(boolean hasFinished) {
    Point pointerPosition = model.getPointer().idxPositionCopy();

    if (!model.hasActiveCommandRequest()) {
      CreateLedPathCommandRequest request =
          CreateLedPathCommandRequest.builder()
              .id(model.generateId(ComponentType.LED_PATH))
              .commandType(ComponentType.LED_PATH)
              .idxPositions(
                  Sets.newLinkedHashSet(
                      Lists.newArrayList(pointerPosition)))
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else if (!hasFinished) {
      CreateLedPathCommandRequest request =
          (CreateLedPathCommandRequest) model.getActiveCommandRequest().get();
      Optional<Point> ledInSamePosition =
          request.getIdxPositions()
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
    Optional<ConnectionComponent> component =
        model.getCreatedComponentsManager()
            .lookupConnectionComponent(model.getPointer().getIdxPosition());
    if (component.isEmpty()) {
      return;
    }

    if (!model.hasActiveCommandRequest()) { // First Interaction
      if (component.get().getNextComponent().isPresent()) {
        return;
      }
      CreateBridgeCommandRequest request =
          CreateBridgeCommandRequest.builder()
              .id(model.generateId(ComponentType.BRIDGE))
              .commandType(ComponentType.BRIDGE)
              .startComponent(component.get())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      CreateBridgeCommandRequest request =
          ((CreateBridgeCommandRequest) model.getActiveCommandRequest().get());
      // Avoid adding end bridge to a Led component which already contains an end bridge, and also
      // avoid adding a bridge to the component itself.
      if (component.get().getPreviousComponent().isPresent() ||
          request.getStartComponent() == component.get()) {
        return;
      }
      request = request
          .toBuilder()
          .endComponent(component.get())
          .build();
      commandManager.execute(new CreateBridgeCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }

  public void cancel() {
    model.setActiveCommandRequest(Optional.empty());
  }
}
