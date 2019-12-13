package com.kyoo.pixel.views.project;

import com.google.inject.Inject;
import com.kyoo.pixel.KeyboardHandler;
import com.kyoo.pixel.inject.Controller;
import com.kyoo.pixel.utils.fx.SceneTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainProject extends Controller {

  @FXML private StackPane projectPane;

  @Inject
  public MainProject(SceneTransition sceneTransition, KeyboardHandler keyboardHandler) {
    super(sceneTransition, keyboardHandler);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    Parent parent =
        sceneTransition.loadFragment(
            StartProjectScene.class.getResource("scene_start_project.fxml"), this);
    parent.setId("scene_start_project");
    projectPane.getChildren().add(parent);
  }

  public void switchScene(String newChildId, URL childUrl) {
    switchScene(projectPane, newChildId, childUrl);
  }
}
