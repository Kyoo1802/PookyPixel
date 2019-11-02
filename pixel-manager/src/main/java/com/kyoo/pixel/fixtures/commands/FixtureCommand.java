package com.kyoo.pixel.fixtures.commands;

public interface FixtureCommand {

  boolean execute();

  void undo();
}
