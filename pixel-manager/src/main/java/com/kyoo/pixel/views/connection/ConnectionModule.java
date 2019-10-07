package com.kyoo.pixel.views.connection;

import com.google.inject.AbstractModule;

/**
 *
 */
public class ConnectionModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ConnectionView.class);
    bind(ConnectionViewModel.class);
    bind(ConnectionModel.class);
  }
}
