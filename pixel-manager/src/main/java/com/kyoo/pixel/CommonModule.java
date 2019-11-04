package com.kyoo.pixel;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.util.ResourceBundle;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommonModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MainKeyHandler.class);
  }

  @Provides
  private ResourceBundle loadProperties() {
    return ResourceBundle.getBundle("messages");
  }
}
