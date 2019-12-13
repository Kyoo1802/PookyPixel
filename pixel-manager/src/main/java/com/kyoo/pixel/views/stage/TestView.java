package com.kyoo.pixel.views.stage;

import com.google.inject.Inject;
import com.kyoo.pixel.KeyboardHandler;
import com.kyoo.pixel.inject.Controller;
import com.kyoo.pixel.paint.SlideConfiguration;
import com.kyoo.pixel.paint.SlideManager;
import com.kyoo.pixel.utils.fx.SceneTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TestView extends Controller {
  @FXML private StackPane testPane;

  private SlideManager slideManager;

  @Inject
  public TestView(SceneTransition sceneTransition, KeyboardHandler keyboardHandler) {
    super(sceneTransition, keyboardHandler);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    slideManager = SlideManager.of(SlideConfiguration.defaultValues(), testPane, keyboardHandler);
  }
}
