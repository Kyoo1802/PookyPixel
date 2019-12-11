package com.kyoo.pixel.views.stage.fixture.commands;

public interface FixtureCommand {

  boolean execute();

  void undo();
}
