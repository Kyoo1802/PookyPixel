package com.kyoo.pixel.views.stage.fixture.commands;

import com.kyoo.pixel.utils.draw.DrawCommandUtils;
import com.kyoo.pixel.views.stage.fixture.FixtureModel;
import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import com.kyoo.pixel.views.stage.fixture.components.Component;
import com.kyoo.pixel.views.stage.fixture.components.ComponentKey;
import com.kyoo.pixel.views.stage.fixture.paint.InteractiveBridge;
import javafx.scene.canvas.GraphicsContext;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;

@Log4j2
public final class CreateBridgeCommand implements FixtureCommand {

  private final FixtureModel fixtureModel;
  private final CreateBridgeCommandRequest request;
  private InteractiveBridge iBridge;

  public CreateBridgeCommand(FixtureModel fixtureModel, CreateBridgeCommandRequest request) {
    this.fixtureModel = fixtureModel;
    this.request = request;
  }

  @Override
  public boolean execute() {
    if (iBridge != null) {
      iBridge = InteractiveBridge.from(request);
    }
    fixtureModel.joinComponents(iBridge);
    log.debug("Draw Connection Port triggered %s", request);
    return true;
  }

  @Override
  public void undo() {
    fixtureModel.separateComponents(iBridge);
    log.debug(
        "Draw Delete Connection Port triggered %s-%s", request.getComponentKey(), request.getId());
  }

  @Getter
  @Builder(toBuilder = true)
  public static class CreateBridgeCommandRequest extends FixtureCommandRequest {

    private long id;
    private ComponentKey componentKey;
    private Component startComponent;
    private Component endComponent;

    @Override
    public void draw(GraphicsContext gc, FixtureProperties properties, Point pointer) {
      DrawCommandUtils.drawBridgeCommand(gc, properties, this, pointer);
    }
  }
}
