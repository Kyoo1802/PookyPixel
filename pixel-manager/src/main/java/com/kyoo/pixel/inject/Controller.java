package com.kyoo.pixel.inject;

import com.kyoo.pixel.KeyboardHandler;
import com.kyoo.pixel.utils.fx.SceneTransition;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.net.URL;

public abstract class Controller implements Initializable {
  protected final SceneTransition sceneTransition;
  protected final KeyboardHandler keyboardHandler;
  @Getter @Setter @Nullable protected Controller parent;

  public Controller(SceneTransition sceneTransition, KeyboardHandler keyboardHandler) {
    this.sceneTransition = sceneTransition;
    this.keyboardHandler = keyboardHandler;
  }

  protected void switchScene(Pane containerId, String newChildId, URL childUrl) {
    sceneTransition.switchScene(containerId, newChildId, childUrl);
  }
}
