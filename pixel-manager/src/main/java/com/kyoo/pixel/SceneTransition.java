package com.kyoo.pixel;

import com.google.common.collect.Maps;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.kyoo.pixel.inject.GuiceFXMLLoader;
import java.util.Map;
import java.util.Optional;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;

@Singleton
public class SceneTransition {

  @Getter
  private Scene scene;
  private GuiceFXMLLoader loader;
  private Stage stage;

  public static void switchStackPane(StackPane stageStackPane, String idNext) {
    Node currentElement = getCurrentElement(stageStackPane);
    Node nextElement = getElement(stageStackPane, idNext);
    applyTransition(currentElement, nextElement);
  }

  public static Node getCurrentElement(StackPane stageStackPane) {
    return stageStackPane.getChildren()
        .parallelStream()
        .filter(n -> n.isVisible())
        .findFirst()
        .get();
  }

  public static Node getElement(StackPane stageStackPane, String id) {
    return stageStackPane.getChildren()
        .parallelStream()
        .filter(n -> n.getId().equals(id))
        .findFirst()
        .get();
  }

  private static void applyTransition(Node currentElement, Node nextElement) {
    currentElement.setVisible(true);
    nextElement.setVisible(true);
    FadeTransition ft1 = new FadeTransition(Duration.millis(500), nextElement);
    ft1.setFromValue(0);
    ft1.setToValue(1);
    FadeTransition ft2 = new FadeTransition(Duration.millis(500), currentElement);
    ft2.setFromValue(1);
    ft2.setToValue(0);
    ParallelTransition pt = new ParallelTransition(ft1, ft2);
    pt.setOnFinished(t ->
    {
      currentElement.setVisible(false);
      nextElement.setVisible(true);
    });
    pt.play();
    currentElement.toBack();
  }

  public void init(Injector injector, Stage stage, StageStyle style, String scenePath) {
    loader = new GuiceFXMLLoader(injector);
    this.scene = new Scene(loadNode(scenePath));
    this.scene
        .getStylesheets()
        .add(getClass().getResource("stylesheets/style.css").toExternalForm());
    this.scene.setOnKeyReleased(injector.getInstance(MainKeyHandler.class));

    this.stage = stage;
    this.stage.setTitle("Pixelandia");
    this.stage.setScene(scene);
    this.stage.initStyle(style);
  }

  public Parent loadNode(String path) {
    return loadNode(path, Maps.newHashMap());
  }

  public Parent loadNode(String path, Map<String, Object> parameters) {
    return loader.load(getClass().getResource(path), parameters, Optional.empty());
  }

  public <T> Parent loadNode(String path, Map<String, Object> parameters, Optional<T> request) {
    return loader.load(getClass().getResource(path), parameters, request);
  }


  public void switchScene(String scenePath, boolean maximize) {
    Parent newScene = loadNode(scenePath);
    scene.setRoot(newScene);
    if (maximize) {
      maximize();
    } else {
      sizeToScene();
    }
  }

  public void switchStage(String stagePath, StageStyle style, boolean maximize) {
    switchStage(stagePath, style, maximize, Optional.empty());
  }

  public <T> void switchStage(String stagePath, StageStyle style, boolean maximize,
      Optional<T> request) {
    Stage newStage = new Stage(style);
    Scene newScene = new Scene(loadNode(stagePath, Maps.newHashMap(), request));
    newScene
        .getStylesheets()
        .add(getClass().getResource("stylesheets/style.css").toExternalForm());
    newStage.setScene(newScene);
    newStage.show();
    this.stage.close();
    this.stage = newStage;
    this.scene = newScene;
    if (style == StageStyle.UNDECORATED) {
      this.stage.setResizable(false);
    }
    if (maximize) {
      maximize();
    } else {
      sizeToScene();
    }
  }

  private void maximize() {
    stage.setMaximized(true);
  }

  public void sizeToScene() {
    this.stage.sizeToScene();
    this.stage.centerOnScreen();
  }

  public Stage getStage() {
    return stage;
  }
}
