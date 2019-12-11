package com.kyoo.pixel.views.project;

import com.google.inject.Inject;
import com.kyoo.pixel.inject.Controller;
import com.kyoo.pixel.model.ProjectMeta;
import com.kyoo.pixel.utils.fx.SceneTransition;
import com.kyoo.pixel.utils.fx.StageProperties;
import com.kyoo.pixel.utils.io.PixelandiaPaths;
import com.kyoo.pixel.utils.io.ProjectMetaIO;
import com.kyoo.pixel.utils.io.StageMetaIO;
import com.kyoo.pixel.views.stage.MainStage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreateProjectScene extends Controller {

  @FXML private TextField projectPathTf;
  @FXML private TextField projectNameTf;
  @FXML private Label errorLbl;
  @FXML private Button createProjectBtn;

  private ResourceBundle resources;

  @Inject
  public CreateProjectScene(SceneTransition sceneTransition, ResourceBundle resources) {
    super(sceneTransition);
    this.resources = resources;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    projectPathTf.setText(PixelandiaPaths.getHomeDir());
    createProjectBtn.setOnMouseClicked(e -> createProject());
    projectNameTf.setOnKeyTyped(e -> onTypeProjectName());
  }

  public void createProject() {
    if (projectNameTf.getText().isEmpty()) {
      errorLbl.setVisible(true);
      errorLbl.setText(resources.getString("createproject.error.empty"));
    } else if (projectNameTf.getText().length() > 50) {
      errorLbl.setVisible(true);
      errorLbl.setText(resources.getString("createproject.error.length"));
    } else if (Files.exists(Paths.get(getProjectPath()))) {
      errorLbl.setVisible(true);
      errorLbl.setText(resources.getString("createproject.error.exists"));
    } else {
      ProjectMeta projectMeta =
          ProjectMeta.builder().name(projectNameTf.getText()).path(getProjectPath()).build();
      ProjectMetaIO.appendProjectMeta(projectMeta);
      StageMetaIO.createStageFile(projectMeta);
      createProjectBtn.setDisable(true);
      sceneTransition.switchStage(
          MainStage.class.getResource("main_stage.fxml"),
          StageProperties.defaultValues(),
          Optional.of(projectMeta));
    }
  }

  public void onTypeProjectName() {
    errorLbl.setVisible(false);
    projectPathTf.setText(getProjectPath());
  }

  private String getProjectPath() {
    return PixelandiaPaths.getHomeDir() + "/" + projectNameTf.getText();
  }
}
