package com.kyoo.pixel.connection.handlers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.LedComponent;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawDriverPortRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawLedBridgeCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawLedPathCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawSquarePanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.DrawDriverPortCommand;
import com.kyoo.pixel.connection.components.commands.DrawLedBridgeCommand;
import com.kyoo.pixel.connection.components.commands.DrawSquarePanelCommand;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class DrawingCommandHandler {

  private ConnectionViewModel viewModel;
  private ConnectionModel model;

  public DrawingCommandHandler(ConnectionViewModel viewModel) {
    this.viewModel = viewModel;
    this.model = viewModel.getModel();
  }

  public void handleSquarePanelDrawing() {
    if (model.hasActiveCommandRequest()) {
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
      viewModel.executeCommand(new DrawSquarePanelCommand(model, request));
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
    viewModel.executeCommand(new DrawDriverPortCommand(model, request));
  }

  public void handleLedPathDrawing() {
    if (model.hasActiveCommandRequest()) {
      DrawLedPathCommandRequest request =
          DrawLedPathCommandRequest.builder()
              .id(model.generateId(ComponentType.LED_PATH))
              .commandType(ComponentType.LED_PATH)
              .idxPositions(
                  Sets.newLinkedHashSet(Lists.newArrayList(model.getPointerCopy())))
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      DrawLedPathCommandRequest request =
          ((DrawLedPathCommandRequest) model.getActiveCommandRequest().get());
      request.getIdxPositions().add(model.getPointerCopy());
    }
  }

  public void handleLedBridgeDrawing() {
    Optional<LedComponent> component =
        model.getCreatedComponentsManager().lookupLedComponent(model.getPointer());
    if (component.isEmpty()) {
      return;
    }

    if (model.hasActiveCommandRequest()) {
      if (component.get().getStartBridge().isPresent()) {
        return;
      }
      DrawLedBridgeCommandRequest request =
          DrawLedBridgeCommandRequest.builder()
              .id(model.generateId(ComponentType.LED_BRIDGE))
              .commandType(ComponentType.LED_BRIDGE)
              .startComponent(component.get())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      if (component.get().getEndBridge().isPresent()) {
        return;
      }
      DrawLedBridgeCommandRequest request =
          ((DrawLedBridgeCommandRequest) model.getActiveCommandRequest().get())
              .toBuilder()
              .endComponent(component.get())
              .build();
      viewModel.executeCommand(new DrawLedBridgeCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }
}
