package com.kyoo.pixel.project;

import com.google.inject.Inject;
import com.kyoo.pixel.SceneTransition;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

public class ManageProjectView implements Initializable {

  private final SceneTransition sceneTransition;

  @FXML
  private StackPane projectPane;

  @Inject
  public ManageProjectView(SceneTransition sceneTransition) {
    this.sceneTransition = sceneTransition;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
  }

  @FXML
  public void createProject() {
    sceneTransition.switchScene("ui/createProjectUI.fxml", false);
  }
}
