package com.kyoo.pixel.views.stage;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.kyoo.pixel.inject.Controller;
import com.kyoo.pixel.inject.ControllerRequest;
import com.kyoo.pixel.model.ProjectMeta;
import com.kyoo.pixel.utils.fx.SceneTransition;
import com.kyoo.pixel.views.stage.fixture.FixtureView;
import com.kyoo.pixel.views.stage.metastage.MetaStageView;
import com.kyoo.pixel.views.stage.visualizer.VisualizerView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import lombok.Builder;
import lombok.Getter;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public final class MainStage extends Controller implements ControllerRequest<ProjectMeta> {

  private static final Map<String, SceneMeta> metaStageMapping = Maps.newHashMap();

  static {
    SceneMeta metaStagePane =
        SceneMeta.builder()
            .id("view_meta_stage")
            .url(MetaStageView.class.getResource("view_meta_stage.fxml"))
            .previousId("")
            .nextId("view_visualizer")
            .build();
    SceneMeta fixturePane =
        SceneMeta.builder()
            .id("view_fixture")
            .url(FixtureView.class.getResource("view_fixture.fxml"))
            .previousId("view_meta_stage")
            .nextId("view_meta_stage")
            .build();
    SceneMeta stagePane =
        SceneMeta.builder()
            .id("view_visualizer")
            .url(VisualizerView.class.getResource("view_visualizer.fxml"))
            .previousId("view_meta_stage")
            .nextId("")
            .build();
    metaStageMapping.put(metaStagePane.id, metaStagePane);
    metaStageMapping.put(fixturePane.id, fixturePane);
    metaStageMapping.put(stagePane.id, stagePane);
  }

  private ProjectMeta request;

  @FXML private StackPane mainPane;
  @FXML private Button previousSceneBtn;
  @FXML private Button nextSceneBtn;
  @FXML private Label metaStageTitle;

  @Inject
  public MainStage(SceneTransition sceneTransition) {
    super(sceneTransition);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    metaStageTitle.setText(request.getName());
    previousSceneBtn.setOnMouseClicked(e -> previousScene());
    nextSceneBtn.setOnMouseClicked(e -> nextScene());

    Parent parent =
        sceneTransition.loadFragment(MetaStageView.class.getResource("view_meta_stage.fxml"), this);
    parent.setId("view_meta_stage");
    mainPane.getChildren().add(parent);
  }

  @Override
  public void setRequest(ProjectMeta request) {
    this.request = request;
  }

  public void nextScene() {
    Node currentElement = SceneTransition.getCurrentElement(mainPane);
    SceneMeta sceneMeta = metaStageMapping.get(currentElement.getId());
    switchScene(
        mainPane, sceneMeta.getNextId(), metaStageMapping.get(sceneMeta.getNextId()).getUrl());
  }

  public void previousScene() {
    Node currentElement = SceneTransition.getCurrentElement(mainPane);
    SceneMeta sceneMeta = metaStageMapping.get(currentElement.getId());
    switchScene(
        mainPane,
        sceneMeta.getPreviousId(),
        metaStageMapping.get(sceneMeta.getPreviousId()).getUrl());
  }

  public void showScene(String sceneId, URL sceneUrl) {
    switchScene(mainPane, sceneId, sceneUrl);
  }

  @Builder
  @Getter
  public static class SceneMeta {
    private String id;
    private URL url;
    private String previousId;
    private String nextId;
  }
}
