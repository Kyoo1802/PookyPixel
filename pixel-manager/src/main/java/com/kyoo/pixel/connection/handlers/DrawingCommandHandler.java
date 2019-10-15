package com.kyoo.pixel.connection.handlers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawDriverPortRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawLedPathCommandRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawSquarePanelCommandRequest;
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
    if (model.thereIsNotComponentBeingCreated()) {
      DrawSquarePanelCommandRequest request =
          DrawSquarePanelCommandRequest.builder()
              .id(model.generateId(ComponentType.SQUARE_PANEL))
              .commandType(ComponentType.SQUARE_PANEL)
              .startIdxPosition(model.getIdxPointer().getPosition())
              .build();
      model.setBeingCreatedComponent(Optional.of(request));
    } else {
      DrawSquarePanelCommandRequest request =
          ((DrawSquarePanelCommandRequest) model.getBeingCreatedComponent().get())
              .toBuilder()
              .endIdxPosition(model.getIdxPointer().getPosition())
              .build();
      viewModel.executeCommand(new DrawSquarePanelCommand(model, request));
      model.setBeingCreatedComponent(Optional.empty());
    }
  }

  public void handleDriverPortDrawing() {
    DrawDriverPortRequest request =
        DrawDriverPortRequest.builder()
            .id(model.generateId(ComponentType.DRIVER_PORT))
            .commandType(ComponentType.DRIVER_PORT)
            .idxPosition(model.getIdxPointer().getPosition())
            .build();
    viewModel.executeCommand(new DrawDriverPortCommand(model, request));
  }

  public void handleLedPathDrawing() {
    if (model.thereIsNotComponentBeingCreated()) {
      DrawLedPathCommandRequest request =
          DrawLedPathCommandRequest.builder()
              .id(model.generateId(ComponentType.LED_PATH))
              .commandType(ComponentType.LED_PATH)
              .idxPositions(
                  Sets.newLinkedHashSet(Lists.newArrayList(model.getIdxPointer().getPosition())))
              .build();
      model.setBeingCreatedComponent(Optional.of(request));
    } else {
      DrawLedPathCommandRequest request =
          ((DrawLedPathCommandRequest) model.getBeingCreatedComponent().get());
      request.getIdxPositions().add(model.getIdxPointer().getPosition());
    }
  }
}
