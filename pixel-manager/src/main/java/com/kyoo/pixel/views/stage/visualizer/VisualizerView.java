package com.kyoo.pixel.views.stage.visualizer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.KeyboardHandler;
import com.kyoo.pixel.inject.Controller;
import com.kyoo.pixel.utils.fx.SceneTransition;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
@Singleton
public class VisualizerView extends Controller {

  @FXML private Canvas canvasStage;
  @FXML private ScrollPane canvasStageScroll;
  @FXML private TreeView<String> stageElements;
  @FXML private StackPane propertyBtns;
  @FXML private BorderPane createBtns;
  @FXML private BorderPane stagePane;

  @Inject
  public VisualizerView(SceneTransition sceneTransition, KeyboardHandler keyboardHandler) {
    super(sceneTransition, keyboardHandler);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    stagePane
        .widthProperty()
        .addListener(
            (o, oV, nV) -> {
              AnchorPane.setLeftAnchor(
                  propertyBtns, stagePane.getWidth() / 2 - propertyBtns.getWidth() / 2);
              AnchorPane.setLeftAnchor(
                  createBtns, stagePane.getWidth() / 2 - createBtns.getWidth() / 2);
            });

    TreeItem<String> rootItem = new TreeItem<>("Pantalla");
    TreeItem<String> efectos = new TreeItem<>("Elementos");
    efectos.getChildren().add(new TreeItem<>("Matriz 10x20"));
    efectos.getChildren().add(new TreeItem<>("Matriz 30x40"));
    efectos.getChildren().add(new TreeItem<>("Matriz 10x10"));
    efectos.getChildren().add(new TreeItem<>("Tira Leds 30"));
    efectos.getChildren().add(new TreeItem<>("Tira Leds 45"));
    efectos.getChildren().add(new TreeItem<>("Tira Leds 23"));
    efectos.getChildren().add(new TreeItem<>("Estrella 3"));
    efectos.getChildren().add(new TreeItem<>("Esfera"));
    rootItem.getChildren().add(efectos);
    rootItem.setExpanded(true);
    stageElements.setShowRoot(true);
    stageElements.setRoot(rootItem);

    canvasStageScroll.setHbarPolicy(ScrollBarPolicy.ALWAYS);
    canvasStageScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    canvasStageScroll.setContent(canvasStage);

    //    AnimationTimer timer = new AnimationTimer() {
    //      @Override
    //      public void handle(long now) {
    //        GraphicsContext gc = canvasStage.getGraphicsContext2D();
    //        gc.setFill(GRAY);
    //        gc.fillRect(0, 0, canvasStage.getWidth(), canvasStage.getHeight());
    //      }
    //    };
    //    timer.start();
  }
}
