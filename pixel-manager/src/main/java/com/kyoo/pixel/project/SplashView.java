package com.kyoo.pixel.project;

import com.google.inject.Inject;
import com.kyoo.pixel.ImageAssets;
import com.kyoo.pixel.SceneTransition;
import com.kyoo.pixel.fixtures.FixtureProperties;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.StageStyle;

public final class SplashView implements Initializable {

  private final Task copyWorker;
  private final SceneTransition sceneTransition;
  private final FixtureProperties properties;
  @FXML
  private Label splashValue;
  @FXML
  private ProgressBar progressBar;

  @Inject
  public SplashView(SceneTransition sceneTransition, FixtureProperties properties) {
    this.sceneTransition = sceneTransition;
    this.properties = properties;
    this.copyWorker = createWorker();
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    copyWorker
        .messageProperty()
        .addListener((observable, oldValue, newValue) -> splashValue.setText(newValue));
    progressBar.progressProperty().bind(copyWorker.progressProperty());
    progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.doubleValue() == 1) { // When progressbar completed
        sceneTransition.switchStage("ui/manageProjectsUI.fxml", StageStyle.DECORATED, false);
      }
    });
    new Thread(copyWorker).start();
  }

  private Task<Boolean> createWorker() {
    return new Task<>() {
      @Override
      protected Boolean call() throws InterruptedException {
        updateMessage("Loading Images..");
        updateProgress(30, 100);
        ImageAssets.load(properties);

        updateMessage("Loading Effects..");
        Thread.sleep(1000);
        updateProgress(60, 100);

        updateMessage("Loading Projects..");
        Thread.sleep(1000);
        updateProgress(100, 100);
        return true;
      }
    };
  }
}