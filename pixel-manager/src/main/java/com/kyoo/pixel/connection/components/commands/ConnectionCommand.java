package com.kyoo.pixel.connection.components.commands;

public interface ConnectionCommand {

  boolean execute();

  void undo();
}
