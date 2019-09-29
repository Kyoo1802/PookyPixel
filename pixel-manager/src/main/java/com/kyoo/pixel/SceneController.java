package com.kyoo.pixel;

import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class SceneController implements Initializable {

  @FXML
  private Label label;

  private SceneData data;

  @Inject
  public SceneController(SceneData data) {
    this.data = data;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion);
  }

  public final String getTitle() {
    return data.getTitle();
  }
}