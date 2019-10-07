package com.kyoo.pixel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kyoo.pixel.inject.GuiceFXMLLoader;
import com.kyoo.pixel.views.connection.ConnectionModule;
import com.kyoo.pixel.visualizer.VisualizerModule;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainApp extends Application {

  public static final Injector injector = Guice.createInjector(new ConnectionModule(),
      new VisualizerModule());

  public static void main(String[] args) {
    System.setProperty("javafx.animation.fullspeed", "true");
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    log.info("Starting your application.");

    GuiceFXMLLoader loader = new GuiceFXMLLoader(injector);

    Parent root = loader.load(getClass().getResource("ui/connectionUI.fxml"));

    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("stylesheets/style.css").toExternalForm());

    stage.setTitle("JavaFX and Gradle");
    stage.setScene(scene);
    stage.show();
  }
}