package com.kyoo.pixel.connection.interactions;

public interface InputInteraction {

  enum NoActionEvent {
    UNKNOWN, POINTER_MOVE, PRESS, RELEASE, CLICK, DRAG, CANCEL,
  }

  enum CreateEvents {
    UNKNOWN, CREATE, MOVE, COMPLETE, CANCEL,
  }
}
