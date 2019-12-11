package com.kyoo.pixel.views.stage.fixture.interactions;

public interface InputInteraction {

  enum NoActionEvent {
    UNKNOWN,
    POINTER_MOVE,
    PRESS,
    RELEASE,
    CLICK,
    DRAG,
    CANCEL,
    COMPLETE
  }

  enum CreateEvents {
    UNKNOWN,
    CREATE,
    MOVE,
    COMPLETE,
    CANCEL,
  }
}
