package com.kyoo.pixel.connection.handlers;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawPanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.DrawPanelCommand;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Point;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class DrawingCommandsHandler {

  private ConnectionViewModel viewModel;
  private ConnectionModel model;

  public DrawingCommandsHandler(ConnectionViewModel viewModel) {
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
    switch (model.getDrawAction()) {
      case DRAW_SQUARE_PANEL:
        DrawPanelCommandRequest request =
            DrawPanelCommandRequest.builder()
                .id(model.generateId(ComponentType.SQUARE_PANEL))
                .componentType(ComponentType.SQUARE_PANEL)
                .startIdxPosition(actionPosition).build();
        model.setBeingCreatedComponent(Optional.of(request));
        break;
      default:
        log.error("Invalid New draw Action");
    }
  }

  private void continueDrawingCommand(Point actionPosition) {
    switch (model.getDrawAction()) {
      case DRAW_SQUARE_PANEL:
        DrawPanelCommandRequest request =
            ((DrawPanelCommandRequest) model.getBeingCreatedComponent().get())
                .toBuilder()
                .endIdxPosition(actionPosition)
                .build();
        viewModel.executeCommand(new DrawPanelCommand(model, request));
        model.setBeingCreatedComponent(Optional.empty());
        break;
      default:
        log.error("Invalid Continue draw Action");
    }
  }

}
