package com.kyoo.pixel.views.stage.fixture;

import com.google.inject.Singleton;
import com.kyoo.pixel.views.stage.fixture.commands.FixtureCommand;

import java.util.LinkedList;

@Singleton
public final class FixtureCommandManager {

  private final LinkedList<FixtureCommand> actionQueue;
  private final LinkedList<FixtureCommand> actionReverseQueue;

  public FixtureCommandManager() {
    this.actionQueue = new LinkedList<>();
    this.actionReverseQueue = new LinkedList<>();
  }

  public void execute(FixtureCommand action) {
    // Just insert commands which makes a real change
    if (action.execute()) {
      actionQueue.add(action);
    }
  }

  public void unDo() {
    FixtureCommand action = actionQueue.removeLast();
    action.undo();
    actionReverseQueue.add(action);
  }

  public void reDo() {
    FixtureCommand action = actionReverseQueue.removeLast();
    action.execute();
    actionQueue.add(action);
  }
}
