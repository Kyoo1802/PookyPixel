package com.kyoo.pixel.data.connection.actions;

import com.google.inject.Singleton;
import java.util.LinkedList;

@Singleton
public final class ConnectionActionManager {

  private LinkedList<ConnectionAction> actionQueue;
  private LinkedList<ConnectionAction> actionReverseQueue;

  public ConnectionActionManager() {
    this.actionQueue = new LinkedList<>();
    this.actionReverseQueue = new LinkedList<>();
  }

  public void execute(ConnectionAction action) {
    action.execute();
    actionQueue.add(action);
  }

  public void unDo() {
    ConnectionAction action = actionQueue.removeLast();
    action.undo();
    actionReverseQueue.add(action);
  }

  public void reDo() {
    ConnectionAction action = actionReverseQueue.removeLast();
    action.execute();
    actionQueue.add(action);
  }
}
