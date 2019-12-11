package com.kyoo.pixel.views.stage.metastage;

import com.google.inject.Inject;
import com.kyoo.pixel.inject.Controller;
import com.kyoo.pixel.utils.fx.SceneTransition;
import com.kyoo.pixel.views.stage.MainStage;
import com.kyoo.pixel.views.stage.fixture.FixtureView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MetaStageView extends Controller {
  @FXML private Button customDisplayBtn;

  @Inject
  public MetaStageView(SceneTransition sceneTransition) {
    super(sceneTransition);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    customDisplayBtn.setOnMouseClicked(e -> showFixture());
  }

  private void showFixture() {
    ((MainStage) parent)
        .showScene("view_fixture", FixtureView.class.getResource("view_fixture.fxml"));
  }
}
