package com.kyoo.pixel.stage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class StageView implements Initializable {

  @FXML
  private StackPane stageStackPane;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
  }

  @FXML
  public void changeTop() {
    ObservableList<Node> childs = this.stageStackPane.getChildren();

    if (childs.size() > 1) {
      // This node will be brought to the front
      Node newTopNode = childs.get(childs.size() - 2);
      Node topNode = childs.get(childs.size() - 1);
      topNode.setVisible(true);
      newTopNode.setVisible(true);

      FadeTransition ft1 = new FadeTransition(Duration.millis(1000), newTopNode);
      ft1.setFromValue(0);
      ft1.setToValue(1);
      FadeTransition ft2 = new FadeTransition(Duration.millis(1000), topNode);
      ft2.setFromValue(1);
      ft2.setToValue(0);
      ParallelTransition pt = new ParallelTransition(ft1, ft2);
      pt.setOnFinished(t ->
      {
        topNode.setVisible(false);
        newTopNode.setVisible(true);
      });
      pt.play();
      topNode.toBack();

    }
  }
}