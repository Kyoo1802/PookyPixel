package com.kyoo.pixel.project;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.kyoo.pixel.SceneTransition;
import com.kyoo.pixel.utils.files.PixelandiaPaths;
import com.kyoo.pixel.utils.files.ProjectMetaUtils;
import com.kyoo.pixel.utils.files.ProjectMetaUtils.ProjectMeta;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;

public class ManageProjectView implements Initializable {

  private final SceneTransition sceneTransition;
  @FXML
  private VBox loadedProjects;

  @Inject
  public ManageProjectView(SceneTransition sceneTransition) {
    this.sceneTransition = sceneTransition;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    List<ProjectMeta> projects = ProjectMetaUtils.listProjectMetas(true);

    int id = 0;
    for (ProjectMeta project : projects) {
      Map<String, Object> parameters = Maps.newHashMap();
      parameters.put("project", project);

      HBox node = (HBox) sceneTransition.loadNode("ui/managerProjectsButton.fxml", parameters);
      node.setId("btn-" + id);

      Button removeBtn = ((Button) node.lookup("#removeBtn"));
      removeBtn.setOnMouseClicked(e -> removeProject(e, project));

      Button projectBtn = ((Button) node.lookup("#projectBtn"));
      projectBtn.setOnMouseClicked(e -> openProject(project));
      if (!project.exists()) {
        projectBtn
            .getGraphic()
            .lookup("#projectPath")
            .getStyleClass()
            .add("text-error");
      }
      loadedProjects.getChildren().add(node);
    }
  }

  @FXML
  private void removeProject(MouseEvent e, ProjectMeta project) {
    ProjectMetaUtils.removeProject(project);
    loadedProjects.getChildren().remove(((Button) e.getSource()).getParent());
  }

  @FXML
  public void createProject() {
    sceneTransition.switchScene("ui/createProjectUI.fxml", false);
  }

  @FXML
  public void openProject(ProjectMeta projectMeta) {
    if (projectMeta.exists()) {
      ProjectMetaUtils.openProject(projectMeta);
      sceneTransition.switchStage(
          "ui/mainStageUI.fxml",
          StageStyle.DECORATED,
          false,
          Optional.of(projectMeta));
    }
  }

  @FXML
  public void chooserProject() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(Paths.get(PixelandiaPaths.getHomeDir()).toFile());
    fileChooser
        .getExtensionFilters()
        .addAll(
            new FileChooser.ExtensionFilter("Pixelandia Project", "*.pixelandia"));
    File openFile = fileChooser.showOpenDialog(sceneTransition.getStage());
    if (openFile != null) {
      ProjectMeta projectMeta =
          ProjectMeta
              .builder()
              .name(openFile.getName())
              .path(openFile.getPath())
              .build();
      ProjectMetaUtils.appendProject(projectMeta);
      sceneTransition.switchStage(
          "ui/mainStageUI.fxml",
          StageStyle.DECORATED,
          false,
          Optional.of(projectMeta));
    }
  }
}
