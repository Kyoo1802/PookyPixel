package com.kyoo.pixel.connection.handlers;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawDriverPortRequest;
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

  public void handleDrawAction() {
    ;
    switch (model.getConnectionActionState()) {
      case DRAW_SQUARE_PANEL:
        handleSquarePanelDrawing();
        break;
      case DRAW_DRIVER_PORT:
        handleDriverPortDrawing();
        break;
      default:
        log.error("Invalid New draw Action: " + model.getConnectionActionState());
    }
  }

  private void handleDriverPortDrawing() {
    DrawDriverPortRequest portCommandRequest =
        DrawDriverPortRequest.builder()
            .id(model.generateId(ComponentType.DRIVER_PORT))
            .commandType(ComponentType.DRIVER_PORT)
            .idxPosition(model.getIdxPointer().getPosition())
            .build();
    viewModel.executeCommand(new DrawDriverPortCommand(model, portCommandRequest));
  }

  private void handleSquarePanelDrawing() {
    if (model.thereIsNotComponentBeingCreated()) {
      DrawSquarePanelCommandRequest squarePanelRequest =
          DrawSquarePanelCommandRequest.builder()
              .id(model.generateId(ComponentType.SQUARE_PANEL))
              .commandType(ComponentType.SQUARE_PANEL)
              .startIdxPosition(model.getIdxPointer().getPosition())
              .build();
      model.setBeingCreatedComponent(Optional.of(squarePanelRequest));
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
}
