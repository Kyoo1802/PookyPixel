package com.kyoo.pixel.connection.handlers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.LedComponent;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawConnectorPortCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawDriverPortRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawLedPathCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawSquarePanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.DrawConnectorPortCommand;
import com.kyoo.pixel.connection.components.commands.DrawDriverPortCommand;
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

  public void handleConnectorPortDrawing() {
    Optional<LedComponent> component =
        model.getCreatedComponentsManager().lookupLedComponent(model.getPointer());
    if (component.isEmpty()) {
      return;
    }

    if (model.hasActiveCommandRequest()) {
      DrawConnectorPortCommandRequest request =
          DrawConnectorPortCommandRequest.builder()
              .id(model.generateId(ComponentType.CONNECTOR_PORT))
              .commandType(ComponentType.CONNECTOR_PORT)
              .startIdxPosition(component.get().lastLed().getIdxPosition())
              .build();
      model.setActiveCommandRequest(Optional.of(request));
    } else {
      DrawConnectorPortCommandRequest request =
          ((DrawConnectorPortCommandRequest) model.getActiveCommandRequest().get())
              .toBuilder()
              .endIdxPosition(component.get().lastLed().getIdxPosition())
              .build();
      viewModel.executeCommand(new DrawConnectorPortCommand(model, request));
      model.setActiveCommandRequest(Optional.empty());
    }
  }
}
