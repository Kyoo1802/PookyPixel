package com.kyoo.pixel.controllers;

import com.google.inject.Inject;
import com.kyoo.pixel.data.SceneData;
import com.kyoo.pixel.visualizer.Visualizer;
import com.kyoo.pixel.visualizer.components.capturer.ScreenCapturer;
import com.kyoo.pixel.visualizer.data.ImageFrame;
import java.awt.Rectangle;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import lombok.extern.log4j.Log4j2;

@Log4j2
  public class SceneController implements Initializable {

  @FXML
  private Label label;

  @FXML
  private Canvas canvas;

  private SceneData data;
  private ScreenCapturer capturer;
  private Visualizer visualizer;

  @Inject
  public SceneController(Visualizer visualizer, SceneData data, ScreenCapturer capturer) {
    this.visualizer = visualizer;
    this.data = data;
    this.capturer = capturer;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion);
    capturer.updateCaptureWindow(new Rectangle((int)canvas.getWidth(), (int)canvas.getHeight()));

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        Optional<ImageFrame> frame = capturer.getImageFrame();
        if (frame.isPresent()) {
          canvas.getGraphicsContext2D()
              .drawImage(SwingFXUtils.toFXImage(frame.get().getBufferedImage(), null), 0, 0);
        }
      }
    };
    timer.start();
  }

  public final String getTitle() {
    return data.getTitle();
  }
}