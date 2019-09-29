package com.kyoo.pixel;
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
