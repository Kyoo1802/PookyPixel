package com.kyoo.pixel.connection.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.connection.ConnectionModel;
import com.kyoo.pixel.connection.components.ComponentType;
import com.kyoo.pixel.connection.components.commands.ConnectionCommandManager;
import com.kyoo.pixel.connection.components.commands.SelectComponentCommand;
import com.kyoo.pixel.connection.components.commands.SelectComponentCommand.SelectCommandRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class SelectCommandHandler {

  private final ConnectionModel model;
  private final ConnectionCommandManager commandManager;

  @Inject
  public SelectCommandHandler(ConnectionModel model, ConnectionCommandManager commandManager) {
    this.model = model;
    this.commandManager = commandManager;
  }

  public void handleSelectAction() {
    SelectCommandRequest request =
        SelectCommandRequest.builder()
            .id(model.generateId(ComponentType.SELECT))
            .commandType(ComponentType.SELECT)
            .selectIdxPosition(model.getPointer().idxPositionCopy())
            .build();
    commandManager.execute(new SelectComponentCommand(model, request));
  }
}
