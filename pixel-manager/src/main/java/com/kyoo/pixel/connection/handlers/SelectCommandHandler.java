package com.kyoo.pixel.connection.handlers;

import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.ConnectionViewModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandRequest.SelectCommandRequest;
import com.kyoo.pixel.connection.components.commands.SelectCommand;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class SelectCommandHandler {

  private ConnectionViewModel viewModel;
  private ConnectionModel model;

  public SelectCommandHandler(ConnectionViewModel viewModel) {
    this.viewModel = viewModel;
    this.model = viewModel.getModel();
  }

  public void handleSelectAction() {
    SelectCommandRequest request =
        SelectCommandRequest.builder()
            .id(model.generateId(ComponentType.SELECT))
            .commandType(ComponentType.SQUARE_PANEL)
            .selectIdxPosition(model.getIdxPointer().getPosition())
            .build();
    viewModel.executeCommand(new SelectCommand(model, request));
  }

}
