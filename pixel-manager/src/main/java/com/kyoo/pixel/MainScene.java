package com.kyoo.pixel;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.kyoo.pixel.inject.GuiceFXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

@Singleton
public class MainScene {

  @Getter
  private Scene scene;
  private GuiceFXMLLoader loader;
  private Stage stage;

  public void init(Injector injector, Stage stage, String scenePath) {
    loader = new GuiceFXMLLoader(injector);
    this.stage = stage;
    Parent splash = loader.load(getClass().getResource(scenePath));
    scene = new Scene(splash);
    scene.getStylesheets().add(getClass().getResource("stylesheets/style.css").toExternalForm());
    scene.setOnKeyReleased(injector.getInstance(MainReleaseKeyHandler.class));
  }

  public void switchScene(String scenePath) {
    Parent newScene = loader.load(getClass().getResource(scenePath));
    scene.setRoot(newScene);
    stage.setMaximized(true);
  }
}
