package com.kyoo.pixel.manager;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class SplashUIView implements Initializable {

  Task copyWorker;
  @FXML
  private Label label;
  @FXML
  private ProgressBar progressBar;

  @FXML
  private void handleButtonAction(ActionEvent event) {
    progressBar.setProgress(0.0);
    progressBar.progressProperty().bind(copyWorker.progressProperty());
    copyWorker.messageProperty()
        .addListener((observable, oldValue, newValue) -> label.setText(newValue));
    new Thread(copyWorker).start();
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    copyWorker = createWorker();
  }

  public Task<Boolean> createWorker() {
    return new Task<>() {
      @Override
      protected Boolean call() throws Exception {
        updateMessage("Task Completed :  ");
        updateProgress(100, 100);
        return true;
      }
    };
  }
}