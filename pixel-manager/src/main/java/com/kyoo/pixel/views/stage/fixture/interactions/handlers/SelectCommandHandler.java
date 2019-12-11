package com.kyoo.pixel.views.stage.fixture.interactions.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.views.stage.fixture.FixtureCommandManager;
import com.kyoo.pixel.views.stage.fixture.FixtureModel;
import com.kyoo.pixel.views.stage.fixture.commands.SelectComponentCommand;
import com.kyoo.pixel.views.stage.fixture.commands.SelectComponentCommand.SelectCommandRequest;
import com.kyoo.pixel.views.stage.fixture.components.ComponentType;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public final class SelectCommandHandler {

  private final FixtureModel model;
  private final FixtureCommandManager commandManager;

  @Inject
  public SelectCommandHandler(FixtureModel model, FixtureCommandManager commandManager) {
    this.model = model;
    this.commandManager = commandManager;
  }

  public void handleSelectAction() {
    SelectCommandRequest request =
        SelectCommandRequest.builder()
            .id(model.generateCommandId())
            .commandType(ComponentType.SELECT)
            .selectIdxPosition(model.getPointer().idxPositionCopy())
            .build();
    commandManager.execute(new SelectComponentCommand(model, request));
  }
}
