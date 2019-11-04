package com.kyoo.pixel.stage;

import com.kyoo.pixel.SceneTransition;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class MetaStageView implements Initializable {

  @FXML
  private HBox metaStagePane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  public void changeTop() {
    StackPane stageStackPane = (StackPane) this.metaStagePane.getScene().getRoot()
        .lookup("#stageStackPane");
    SceneTransition.switchStackPane(stageStackPane, "fixturePane");
  }
}
