package com.kyoo.pixel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kyoo.pixel.fixtures.FixtureModule;
import com.kyoo.pixel.visualizer.VisualizerModule;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainApp extends Application {

  public static final Injector injector = Guice.createInjector(
      new CommonModule(),
      new FixtureModule(),
      new VisualizerModule());

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    log.info("Starting your application.");

    // Create initial scene
    SceneTransition sceneTransition = injector.getInstance(SceneTransition.class);
    sceneTransition.init(injector, stage, StageStyle.UNDECORATED, "ui/SplashUI.fxml");
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    System.exit(0);
  }

}