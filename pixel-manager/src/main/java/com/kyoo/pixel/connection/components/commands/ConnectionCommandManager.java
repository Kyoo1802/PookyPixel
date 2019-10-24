package com.kyoo.pixel.connection.components.commands;

import com.google.inject.Singleton;
import java.util.LinkedList;

@Singleton
public final class ConnectionCommandManager {

  private final LinkedList<ConnectionCommand> actionQueue;
  private final LinkedList<ConnectionCommand> actionReverseQueue;

  public ConnectionCommandManager() {
    this.actionQueue = new LinkedList<>();
    this.actionReverseQueue = new LinkedList<>();
  }

  public void execute(ConnectionCommand action) {
    // Just insert commands which makes a real change
    if (action.execute()) {
      actionQueue.add(action);
    }
  }

  public void unDo() {
    ConnectionCommand action = actionQueue.removeLast();
    action.undo();
    actionReverseQueue.add(action);
  }

  public void reDo() {
    ConnectionCommand action = actionReverseQueue.removeLast();
    action.execute();
    actionQueue.add(action);
  }
}
