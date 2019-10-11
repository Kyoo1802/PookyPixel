package com.kyoo.pixel.connection.handlers;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.SelectCommandRequest;
import com.kyoo.pixel.connection.components.commands.SelectCommand;
import com.kyoo.pixel.utils.PositionUtils;
import java.awt.Point;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class SelectCommandsHandler {

  private ConnectionViewModel viewModel;
  private ConnectionModel model;

  public SelectCommandsHandler(ConnectionViewModel viewModel) {
    this.viewModel = viewModel;
    this.model = viewModel.getModel();
  }

  public void handleSelectAction() {
    Point actionPosition = PositionUtils.toIdxPosition(viewModel.getMousePosition().get());
    SelectCommandRequest request =
        SelectCommandRequest.builder()
            .id(model.generateId(ComponentType.SELECT))
            .componentType(ComponentType.SQUARE_PANEL)
            .selectIdxPosition(actionPosition)
            .build();
    viewModel.executeCommand(new SelectCommand(model, request));
  }

}
