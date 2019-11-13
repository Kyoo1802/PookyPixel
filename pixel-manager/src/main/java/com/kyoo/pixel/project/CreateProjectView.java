package com.kyoo.pixel.project;

import com.google.inject.Inject;
import com.kyoo.pixel.SceneTransition;
import com.kyoo.pixel.utils.files.PixelandiaPaths;
import com.kyoo.pixel.utils.files.ProjectMetaUtils;
import com.kyoo.pixel.utils.files.ProjectMetaUtils.ProjectMeta;
import com.kyoo.pixel.utils.files.StageMetaUtils;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

public class CreateProjectView implements Initializable {

  private final SceneTransition sceneTransition;

  @FXML
  private TextField projectPath;
  @FXML
  private TextField projectName;
  @FXML
  private Label error;
  @FXML
  private Button createProjectBtn;

  private ResourceBundle resources;

  @Inject
  public CreateProjectView(SceneTransition sceneTransition, ResourceBundle resources) {
    this.sceneTransition = sceneTransition;
    this.resources = resources;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.projectPath.setText(PixelandiaPaths.getHomeDir());

  }

  @FXML
  public void createProjectMeta() {
    if (projectName.getText().isEmpty()) {
      error.setVisible(true);
      error.setText(resources.getString("createproject.error.empty"));
    } else if (projectName.getText().length() > 50) {
      error.setVisible(true);
      error.setText(resources.getString("createproject.error.length"));
    } else if (Files.exists(Paths.get(getProjectPath()))) {
      error.setVisible(true);
      error.setText(resources.getString("createproject.error.exists"));
    } else {
      ProjectMeta projectMeta =
          ProjectMeta.builder().name(projectName.getText()).path(getProjectPath()).build();
      StageMetaUtils.createStage(projectMeta);
      ProjectMetaUtils.appendProject(projectMeta);
      createProjectBtn.setDisable(true);
      sceneTransition.switchStage(
          "ui/mainStageUI.fxml",
          StageStyle.DECORATED,
          false,
          Optional.of(projectMeta));
    }
  }

  @FXML
  public void onTypeProjectName() {
    error.setVisible(false);
    projectPath.setText(getProjectPath());
  }

  private String getProjectPath() {
    return PixelandiaPaths.getHomeDir() + "/" + projectName.getText();
  }
}

