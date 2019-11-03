package com.kyoo.pixel.manager;

import com.google.inject.Inject;
import com.kyoo.pixel.ImageAssets;
import com.kyoo.pixel.MainStage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public final class SplashUIView implements Initializable {

  private final Task copyWorker;
  private final MainStage mainStage;
  @FXML
  private Label label;
  @FXML
  private ProgressBar progressBar;

  @Inject
  public SplashUIView(MainStage mainStage) {
    copyWorker = createWorker();
    this.mainStage = mainStage;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    copyWorker.messageProperty()
        .addListener((observable, oldValue, newValue) -> label.setText(newValue));
    progressBar.progressProperty().bind(copyWorker.progressProperty());
    progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue.doubleValue() == 1) {
            mainStage.switchScene("ui/selectProjectUI.fxml", false);
          }
        }
    );
    new Thread(copyWorker).start();
  }

  private Task<Boolean> createWorker() {
    return new Task<>() {
      @Override
      protected Boolean call() throws InterruptedException {
        updateMessage("Loading Images..");
        updateProgress(30, 100);
        ImageAssets.load();
        Thread.sleep(1000);

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