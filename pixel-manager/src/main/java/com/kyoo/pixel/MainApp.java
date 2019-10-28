package com.kyoo.pixel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kyoo.pixel.connection.ConnectionModule;
import com.kyoo.pixel.visualizer.VisualizerModule;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainApp extends Application {

  public static final Injector injector = Guice.createInjector(
      new CommonModule(),
      new ConnectionModule(),
      new VisualizerModule());

  public static void main(String[] args) {
    System.setProperty("javafx.animation.fullspeed", "true");
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    MainScene mainScene = injector.getInstance(MainScene.class);
    mainScene.init(injector, stage, "ui/SplashUI.fxml");

    log.info("Starting your application.");
    stage.setTitle("Pixelandia");
    stage.setScene(mainScene.getScene());
    stage.centerOnScreen();
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop(); // To change body of generated methods, choose Tools | Templates.
    System.exit(0);
  }
}