package com.kyoo.pixel.inject;

import com.google.inject.Inject;
import com.kyoo.pixel.utils.fx.SceneTransition;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.net.URL;

public abstract class Controller implements Initializable {
  protected final SceneTransition sceneTransition;
  @Getter @Setter @Nullable protected Controller parent;

  @Inject
  public Controller(SceneTransition sceneTransition) {
    this.sceneTransition = sceneTransition;
  }

  protected void switchScene(Pane containerId, String newChildId, URL childUrl) {
    sceneTransition.switchScene(containerId, newChildId, childUrl);
  }
}
