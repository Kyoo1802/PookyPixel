package com.kyoo.pixel.controllers;

import com.google.inject.AbstractModule;

/**
 *
 */
public class SceneModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SceneController.class);
  }
}
