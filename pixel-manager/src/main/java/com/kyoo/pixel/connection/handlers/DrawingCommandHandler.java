package com.kyoo.pixel.connection.handlers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.ConnectionComponent;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandManager;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawBridgeCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawDriverPortRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawLedPathCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawSquarePanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.DrawBridgeCommand;
import com.kyoo.pixel.connection.components.commands.DrawDriverPortCommand;
import com.kyoo.pixel.connection.components.commands.DrawLedPathCommand;
import com.kyoo.pixel.connection.components.commands.DrawSquarePanelCommand;
import com.kyoo.pixel.connection.components.impl.Led;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class DrawingCommandHandler {

  private ConnectionModel model;
  private ConnectionCommandManager commandManager;

  @Inject
  public DrawingCommandHandler(ConnectionModel model, ConnectionCommandManager commandManager) {
    this.model = model;
    this.commandManager = commandManager;
  }

  public void handleSquarePanelDrawing() {
    if (!model.hasActiveCommandRequest()) {
      DrawSquarePanelCommandRequest request =
          DrawSquarePanelCommandRequest.builder()
              .id(model.generateId(ComponentType.SQUARE_PANEL))
              .commandType(ComponentType.SQUARE_PANEL)
              .startIdxPosition(model.getPointerCopy())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      DrawSquarePanelCommandRequest request =
          ((DrawSquarePanelCommandRequest) model.getActiveCommandRequest().get())
              .toBuilder()
              .endIdxPosition(model.getPointerCopy())
              .build();
      commandManager.execute(new DrawSquarePanelCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }

  public void handleDriverPortDrawing() {
    DrawDriverPortRequest request =
        DrawDriverPortRequest.builder()
            .id(model.generateId(ComponentType.DRIVER_PORT))
            .commandType(ComponentType.DRIVER_PORT)
            .idxPosition(model.getPointerCopy())
            .build();
    commandManager.execute(new DrawDriverPortCommand(model, request));
  }

  public void handleLedPathDrawing(boolean hasFinished) {
    if (!model.hasActiveCommandRequest()) {
      DrawLedPathCommandRequest request =
          DrawLedPathCommandRequest.builder()
              .id(model.generateId(ComponentType.LED_PATH))
              .commandType(ComponentType.LED_PATH)
              .idxPositions(
                  Sets.newLinkedHashSet(Lists.newArrayList(new Led(model.getPointerCopy()))))
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else if (!hasFinished) {
      DrawLedPathCommandRequest request =
          (DrawLedPathCommandRequest) model.getActiveCommandRequest().get();
      Optional<Led> ledInSamePosition =
          request.getIdxPositions().parallelStream()
              .filter(led -> led.getStartIdxPosition().equals(model.getPointer())).findAny();
      if (ledInSamePosition.isEmpty()) {
        request.getIdxPositions().add(new Led(model.getPointerCopy()));
      }
    } else {
      DrawLedPathCommandRequest request =
          (DrawLedPathCommandRequest) model.getActiveCommandRequest().get();
      commandManager.execute(new DrawLedPathCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }

  public void handleBridgeDrawing() {
    Optional<ConnectionComponent> component =
        model.getCreatedComponentsManager().lookupConnectionComponent(model.getPointer());
    if (component.isEmpty()) {
      return;
    }

    if (!model.hasActiveCommandRequest()) { // First Interaction
      if (component.get().getStartBridge().isPresent()) {
        return;
      }
      DrawBridgeCommandRequest request =
          DrawBridgeCommandRequest.builder()
              .id(model.generateId(ComponentType.BRIDGE))
              .commandType(ComponentType.BRIDGE)
              .startComponent(component.get())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      DrawBridgeCommandRequest request =
          ((DrawBridgeCommandRequest) model.getActiveCommandRequest().get());
      // Avoid adding end bridge to a Led component which already contains an end bridge, and also
      // avoid adding a bridge to the component itself.
      if (component.get().getEndBridge().isPresent() ||
          request.getStartComponent() == component.get()) {
        return;
      }
      request = request
          .toBuilder()
          .endComponent(component.get())
          .build();
      commandManager.execute(new DrawBridgeCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }
}
