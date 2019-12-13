package com.kyoo.pixel.utils.fx;

import com.google.common.collect.Maps;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.kyoo.pixel.KeyboardHandler;
import com.kyoo.pixel.MainApp;
import com.kyoo.pixel.inject.Controller;
import com.kyoo.pixel.inject.GuiceFXMLLoader;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public final class SceneTransition {

  private GuiceFXMLLoader loader;
  private Stage stage;
  private Injector injector;

  public static Node getCurrentElement(Pane stageStackPane) {
    return stageStackPane
        .getChildren()
        .parallelStream()
        .filter(n -> n.isVisible())
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
    pt.setOnFinished(
        t -> {
          currentElement.setVisible(false);
          nextElement.setVisible(true);
        });
    pt.play();
    currentElement.toBack();
  }

  public void init(Injector injector, Stage stage, StageStyle style, URL sceneUrl) {
    this.injector = injector;
    this.loader = new GuiceFXMLLoader(injector);

    this.stage = stage;
    this.stage.setTitle("Pixelandia");
    this.stage.setScene(newScene(loadFragment(sceneUrl)));
    this.stage.initStyle(style);
    this.stage.centerOnScreen();
  }

  public Parent loadFragment(URL url) {
    return loadFragment(url, null, Maps.newHashMap());
  }

  public Parent loadFragment(URL url, @Nullable Controller parent) {
    return loader.load(url, parent, Maps.newHashMap());
  }

  public Parent loadFragment(URL url, @Nullable Controller parent, Map<String, Object> parameters) {
    return loader.load(url, parent, parameters, Optional.empty());
  }

  private <T> Parent loadFragment(
      URL url, @Nullable Controller parent, Map<String, Object> parameters, Optional<T> request) {
    return loader.load(url, parent, parameters, request);
  }

  public void switchScene(Pane rootPane, String nextId, URL nextSceneUrl) {
    cleanChildren(rootPane, nextId);
    Node currentElement = getCurrentElement(rootPane);
    Node nextElement = getElement(rootPane, nextId, nextSceneUrl);
    applyTransition(currentElement, nextElement);
  }

  private void cleanChildren(Pane rootPane, String nextId) {
    List<Node> nodes =
        rootPane
            .getChildren()
            .parallelStream()
            .filter(
                n -> {
                  return n.getId().equals(nextId) || n.isVisible();
                })
            .collect(Collectors.toList());
    rootPane.getChildren().retainAll(nodes);
  }

  private Node getElement(Pane stageStackPane, String nextSceneId, URL nextSceneUrl) {
    Optional<Node> node =
        stageStackPane
            .getChildren()
            .parallelStream()
            .filter(n -> n.getId().equals(nextSceneId))
            .findFirst();
    if (node.isPresent()) {
      return node.get();
    } else {
      Node n = loadFragment(nextSceneUrl);
      n.setId(nextSceneId);
      stageStackPane.getChildren().add(n);
      return n;
    }
  }

  public <T> void switchStage(URL stagePath, StageProperties stageProperties) {
    switchStage(stagePath, stageProperties, Optional.empty());
  }

  public <T> void switchStage(URL stagePath, StageProperties stageProperties, Optional<T> request) {
    Scene newScene = newScene(loadFragment(stagePath, null, Maps.newHashMap(), request));
    Stage newStage = newStage(newScene, stageProperties);
    newStage.show();
    this.stage.close();
    this.stage = newStage;
  }

  private Scene newScene(Parent parent) {
    Scene newScene = new Scene(parent);
    newScene
        .getStylesheets()
        .add(MainApp.class.getResource("stylesheets/style.css").toExternalForm());
    newScene.onKeyReleasedProperty().set(injector.getInstance(KeyboardHandler.class));
    return newScene;
  }

  private Stage newStage(Scene newScene, StageProperties stageProperties) {
    Stage newStage = new Stage(stageProperties.getStyle());
    newStage.setScene(newScene);
    newStage.setResizable(stageProperties.isResizable());
    newStage.setMaximized(stageProperties.isMaximized());
    if (!stageProperties.isMaximized() && stageProperties.isSizeToScene()) {
      newStage.sizeToScene();
    }
    if (stageProperties.isCenterToScreen()) {
      newStage.centerOnScreen();
    }
    return newStage;
  }

  public Stage getStage() {
    return stage;
  }
}
