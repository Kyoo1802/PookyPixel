package com.kyoo.pixel.views.stage.fixture.interactions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.views.stage.fixture.FixtureModel;
import com.kyoo.pixel.views.stage.fixture.FixtureModel.FixtureAction;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
@Singleton
public final class StateInteraction implements InputInteraction {

  private final FixtureModel model;

  @Inject
  public StateInteraction(FixtureModel model) {
    this.model = model;
  }

  public void handleInteraction(StateInteractionRequest request) {
    switch (request.getState()) {
      case DRAW_SQUARE_PANEL:
        toggle(FixtureAction.CREATE_SQUARE_PANEL);
        break;
      case DRAW_LED_PATH:
        toggle(FixtureAction.CREATE_LED_PATH);
        break;
      case DRAW_CONNECTOR_PORT:
        toggle(FixtureAction.CREATE_LED_BRIDGE);
        break;
      default:
        log.error("Invalid interaction: " + request.getState());
    }
    model.setActiveCommandRequest(Optional.empty());
  }

  private void toggle(FixtureAction action) {
    if (model.getStateAction() == action) {
      model.setStateAction(FixtureAction.NO_ACTION);
    } else {
      model.setStateAction(action);
    }
  }
}
