package com.kyoo.pixel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kyoo.pixel.connection.ConnectionModule;
import com.kyoo.pixel.inject.GuiceFXMLLoader;
import com.kyoo.pixel.visualizer.VisualizerModule;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    log.info("Starting your application.");
    GuiceFXMLLoader loader = new GuiceFXMLLoader(injector);
    Parent root = loader.load(getClass().getResource("ui/splashUI.fxml"));
    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("stylesheets/style.css").toExternalForm());
    stage.setTitle("JavaFX and Gradle");
    stage.setScene(scene);
    scene.setOnKeyReleased(injector.getInstance(MainReleaseKeyHandler.class));
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop(); //To change body of generated methods, choose Tools | Templates.
    System.exit(0);
  }
}