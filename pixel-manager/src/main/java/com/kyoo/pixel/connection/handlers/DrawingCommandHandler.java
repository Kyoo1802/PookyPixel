package com.kyoo.pixel.connection.handlers;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawDriverPortRequest;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawSquarePanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.DrawDriverPortCommand;
import com.kyoo.pixel.connection.components.commands.DrawSquarePanelCommand;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Point;
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
    Point actionPosition = PositionUtils.toIdxPosition(viewModel.getMousePosition().get());
    if (isNewDrawing()) {
      startNewDrawing(actionPosition);
    } else {
      continueDrawingCommand(actionPosition);
    }
  }

  private boolean isNewDrawing() {
    return model.getBeingCreatedComponent().isEmpty();
  }

  private void startNewDrawing(Point actionPosition) {
    switch (model.getDrawActionState()) {
      case DRAW_SQUARE_PANEL:
        DrawSquarePanelCommandRequest squarePanelRequest =
            DrawSquarePanelCommandRequest.builder()
                .id(model.generateId(ComponentType.SQUARE_PANEL))
                .componentType(ComponentType.SQUARE_PANEL)
                .startIdxPosition(actionPosition)
                .build();
        model.setBeingCreatedComponent(Optional.of(squarePanelRequest));
        break;
      case DRAW_DRIVER_PORT:
        DrawDriverPortRequest portCommandRequest =
            DrawDriverPortRequest.builder()
                .id(model.generateId(ComponentType.DRIVER_PORT))
                .componentType(ComponentType.DRIVER_PORT)
                .idxPosition(actionPosition)
                .build();
        viewModel.executeCommand(new DrawDriverPortCommand(model, portCommandRequest));
        break;
      default:
        log.error("Invalid New draw Action");
    }
  }

  private void continueDrawingCommand(Point actionPosition) {
    switch (model.getDrawActionState()) {
      case DRAW_SQUARE_PANEL:
        DrawSquarePanelCommandRequest request =
            ((DrawSquarePanelCommandRequest) model.getBeingCreatedComponent().get())
                .toBuilder()
                .endIdxPosition(actionPosition)
                .build();
        viewModel.executeCommand(new DrawSquarePanelCommand(model, request));
        model.setBeingCreatedComponent(Optional.empty());
        break;
      default:
        log.error("Invalid Continue draw Action");
    }
  }

}
