package com.kyoo.pixel;
import com.google.inject.AbstractModule;

/**
 *
 * @author Richard
 */
public class SceneModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SceneController.class);
    }
}
