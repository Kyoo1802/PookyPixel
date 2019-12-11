package com.kyoo.pixel.views.project;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.kyoo.pixel.inject.Controller;
import com.kyoo.pixel.model.ProjectMeta;
import com.kyoo.pixel.utils.fx.SceneTransition;
import com.kyoo.pixel.utils.fx.StageProperties;
import com.kyoo.pixel.utils.io.PixelandiaPaths;
import com.kyoo.pixel.utils.io.ProjectMetaIO;
import com.kyoo.pixel.views.stage.MainStage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class StartProjectScene extends Controller {
  @FXML private Button createProjectBtn;
  @FXML private Button openProjectBtn;
  @FXML private VBox openedProjectsView;

  @Inject
  public StartProjectScene(SceneTransition sceneTransition) {
    super(sceneTransition);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    createProjectBtn.setOnMouseClicked(e -> createProject());
    openProjectBtn.setOnMouseClicked(e -> openProject());
    addOpenedProjects();
  }

  private void addOpenedProjects() {
    List<ProjectMeta> openedProjects = ProjectMetaIO.listProjectMetas(true);
    int id = 0;
    for (ProjectMeta projectMeta : openedProjects) {
      Map<String, Object> parameters = Maps.newHashMap();
      parameters.put("project", projectMeta);

      HBox openedProjectView =
          (HBox)
              sceneTransition.loadFragment(
                  StartProjectScene.class.getResource("view_opened_project.fxml"),
                  null,
                  parameters);
      openedProjectView.setId("btn-" + id);

      Button removeBtn = ((Button) openedProjectView.lookup("#removeBtn"));
      removeBtn.setOnMouseClicked(e -> removeProject(e, projectMeta));

      Button projectBtn = ((Button) openedProjectView.lookup("#projectBtn"));
      projectBtn.setOnMouseClicked(e -> openProject(projectMeta));

      if (!projectMeta.pathExists()) {
        projectBtn.getGraphic().lookup("#projectPath").getStyleClass().add("text-error");
      }
      openedProjectsView.getChildren().add(openedProjectView);
    }
  }

  public void createProject() {
    ((MainProject) getParent())
        .switchScene("scene_create_project", getClass().getResource("scene_create_project.fxml"));
  }

  public void openProject() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(Paths.get(PixelandiaPaths.getHomeDir()).toFile());
    fileChooser
        .getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("Pixelandia Project", "*.pixelandia"));
    File openFile = fileChooser.showOpenDialog(sceneTransition.getStage());
    if (openFile != null) {
      ProjectMeta projectMeta =
          ProjectMeta.builder().name(openFile.getName()).path(openFile.getPath()).build();
      ProjectMetaIO.appendProjectMeta(projectMeta);
      sceneTransition.switchStage(
          MainStage.class.getResource("main_stage.fxml"),
          StageProperties.defaultValues(),
          Optional.of(projectMeta));
    }
  }

  public void openProject(ProjectMeta projectMeta) {
    if (projectMeta.pathExists()) {
      ProjectMetaIO.openProject(projectMeta);
      sceneTransition.switchStage(
          MainStage.class.getResource("main_stage.fxml"),
          StageProperties.defaultValues(),
          Optional.of(projectMeta));
    }
  }

  private void removeProject(MouseEvent e, ProjectMeta project) {
    ProjectMetaIO.removeProject(project);
    openedProjectsView.getChildren().remove(((Button) e.getSource()).getParent());
  }
}
