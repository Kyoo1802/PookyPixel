package com.kyoo.pixel;

import com.google.inject.AbstractModule;

public class CommonModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MainReleaseKeyHandler.class);
  }

}
