package com.kyoo.pixel.project;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.kyoo.pixel.SceneTransition;
import com.kyoo.pixel.utils.ProjectUtils;
import com.kyoo.pixel.utils.ProjectUtils.ProjectMeta;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

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
    List<ProjectMeta> projects = ProjectUtils.listProjectMetas();
    for (ProjectMeta project : projects) {
      Map<String, Object> parameters = Maps.newHashMap();
      parameters.put("project", project);
      HBox node = (HBox) sceneTransition.loadNode("ui/managerProjectsButton.fxml", parameters);
      loadedProjects.getChildren().add(node);
    }
  }

  @FXML
  public void createProject() {
    sceneTransition.switchScene("ui/createProjectUI.fxml", false);
  }

  @FXML
  public void openProject() {
  }

  @FXML
  public void chooserProject() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Pixelandia Project", "*.pixelandia")
    );
    fileChooser.setInitialDirectory(Paths.get(ProjectUtils.getHomeDir()).toFile());
    File openFile = fileChooser.showOpenDialog(sceneTransition.getStage());
    sceneTransition.switchScene("ui/createProjectUI.fxml", false);
  }
}
