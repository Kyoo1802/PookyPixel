package com.kyoo.pixel;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.kyoo.pixel.inject.GuiceFXMLLoader;
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
public class ViewTransitioner {

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

  public void init(Injector injector, Stage stage, String scenePath) {
    loader = new GuiceFXMLLoader(injector);
    this.stage = stage;
    Parent splash = loader.load(getClass().getResource(scenePath));
    scene = new Scene(splash);
    scene.getStylesheets().add(getClass().getResource("stylesheets/style.css").toExternalForm());
    scene.setOnKeyReleased(injector.getInstance(MainKeyHandler.class));
  }

  public void switchStage(String stagePath, StageStyle style, boolean maximize) {
    Stage newStage = new Stage(style);
    Scene newScene = new Scene(loader.load(getClass().getResource(stagePath)));
    newStage.setScene(newScene);
    newStage.show();
    this.stage.close();
    this.stage = newStage;
    this.scene = newScene;
    if (maximize) {
      maximize();
    } else {
      sizeToScene();
    }
  }

  public void switchScene(String scenePath, boolean maximize) {
    Parent newNode = loader.load(getClass().getResource(scenePath));
    scene.setRoot(newNode);
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
}