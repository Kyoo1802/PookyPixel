package com.kyoo.pixel.views.project;

import com.google.inject.Inject;
import com.kyoo.pixel.KeyboardHandler;
import com.kyoo.pixel.inject.Controller;
import com.kyoo.pixel.utils.ImageResourceLoader;
import com.kyoo.pixel.utils.fx.SceneTransition;
import com.kyoo.pixel.utils.fx.StageProperties;
import com.kyoo.pixel.views.stage.fixture.FixtureProperties;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

public final class IntroSplash extends Controller {

  private final Task progressBarTask;
  private final FixtureProperties properties;
  @FXML private Label splashValue;
  @FXML private ProgressBar progressBar;

  @Inject
  public IntroSplash(
      FixtureProperties properties,
      SceneTransition sceneTransition,
      KeyboardHandler keyboardHandler) {
    super(sceneTransition, keyboardHandler);
    this.properties = properties;
    this.progressBarTask = progressBarTask();
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    progressBarTask
        .messageProperty()
        .addListener((observable, oldValue, newValue) -> splashValue.setText(newValue));
    progressBar.progressProperty().bind(progressBarTask.progressProperty());
    progressBar
        .progressProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue.doubleValue() == 1) { // When progressbar completed
                sceneTransition.switchStage(
                    StartProjectScene.class.getResource("main_project.fxml"),
                    StageProperties.defaultValuesBuilder()
                        .resizable(false)
                        .maximized(false)
                        .build());
              }
            });
    new Thread(progressBarTask).start();
  }

  private Task<Boolean> progressBarTask() {
    return new Task<>() {
      @Override
      protected Boolean call() throws InterruptedException {
        updateMessage("Loading Images..");
        updateProgress(30, 100);
        ImageResourceLoader.load(properties);

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
