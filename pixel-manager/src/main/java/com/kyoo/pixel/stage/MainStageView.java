package com.kyoo.pixel.stage;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kyoo.pixel.SceneTransition;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

@Singleton
public final class MainStageView implements Initializable {

  private static final Map<String, String> previousPaneMapping = Maps.newHashMap();
  private static final Map<String, String> nextPaneMapping = Maps.newHashMap();

  static {
    previousPaneMapping.put("metaStagePane", "");
    previousPaneMapping.put("fixturePane", "metaStagePane");
    previousPaneMapping.put("stagePane", "metaStagePane");
    nextPaneMapping.put("metaStagePane", "stagePane");
    nextPaneMapping.put("fixturePane", "metaStagePane");
    nextPaneMapping.put("stagePane", "");
  }

  private final SceneTransition sceneTransition;
  @FXML
  private StackPane stageStackPane;

  @Inject
  public MainStageView(SceneTransition sceneTransition) {
    this.sceneTransition = sceneTransition;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
  }

  @FXML
  public void nextPane() {
    Node currentElement = SceneTransition.getCurrentElement(stageStackPane);
    SceneTransition.switchStackPane(stageStackPane, nextPaneMapping.get(currentElement.getId()));
  }

  @FXML
  public void previousPane() {
    Node currentElement = SceneTransition.getCurrentElement(stageStackPane);
    SceneTransition
        .switchStackPane(stageStackPane, previousPaneMapping.get(currentElement.getId()));
  }
}