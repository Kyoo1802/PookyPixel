package com.kyoo.pixel.connection.handlers;

import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.DrawPanelCommandRequest;
import com.kyoo.pixel.connection.components.commands.DrawPanelCommand;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Point;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class DrawingCommandHandler {

  private ConnectionViewModel viewModel;

  public DrawingCommandHandler(ConnectionViewModel viewModel) {
    this.viewModel = viewModel;
  }

  public void handleDrawAction() {
    Point actionPosition = PositionUtils.toIdxPosition(viewModel.getMousePosition().get());
    if (isNewDrawing()) {
      handleNewDrawing(actionPosition);
    } else {
      continueDrawingCommand(actionPosition);
    }
  }

  private boolean isNewDrawing() {
    return viewModel.getModel().getBeingCreatedComponent().isEmpty();
  }

  private void handleNewDrawing(Point actionPosition) {
    switch (viewModel.getModel().getDrawAction()) {
      case DRAW_SQUARE_PANEL:
        DrawPanelCommandRequest request =
            DrawPanelCommandRequest.builder()
                .id(viewModel.getModel().generateId(ComponentType.SQUARE_PANEL))
                .componentType(ComponentType.SQUARE_PANEL)
                .startIdxPosition(actionPosition).build();
        viewModel.getModel().setBeingCreatedComponent(Optional.of(request));
        break;
      default:
        log.error("Invalid New draw Action");
    }
  }

  private void continueDrawingCommand(Point actionPosition) {
    switch (viewModel.getModel().getDrawAction()) {
      case DRAW_SQUARE_PANEL:
        DrawPanelCommandRequest request =
            ((DrawPanelCommandRequest) viewModel.getModel().getBeingCreatedComponent().get())
                .toBuilder()
                .endIdxPosition(actionPosition)
                .build();
        DrawPanelCommand panelAction = new DrawPanelCommand(viewModel.getModel(), request);
        viewModel.getActionManager().execute(panelAction);
        viewModel.getModel().setBeingCreatedComponent(Optional.empty());

        break;
      default:
        log.error("Invalid Continue draw Action");
    }
  }

}
