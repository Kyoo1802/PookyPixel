package com.kyoo.pixel.manager;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ManagerView implements Initializable {

  @FXML
  TreeView<String> proyectoTv;
  @FXML
  Accordion accordion2;
  @FXML
  TitledPane pane2;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    TreeItem<String> rootItem = new TreeItem<>("KyooCon");
    TreeItem<String> conexion = new TreeItem<>("Conexion");
    rootItem.getChildren().add(conexion);
    TreeItem<String> escenas = new TreeItem<>("Visualizador");
    rootItem.getChildren().add(escenas);
    TreeItem<String> efectos = new TreeItem<>("Efectos");
    efectos.getChildren().add(new TreeItem<>("Estrellas"));
    efectos.getChildren().add(new TreeItem<>("Fuego"));
    efectos.getChildren().add(new TreeItem<>("Nombre"));
    rootItem.getChildren().add(efectos);
    rootItem.setExpanded(true);
    proyectoTv.setShowRoot(true);
    proyectoTv.setRoot(rootItem);
    accordion2.setExpandedPane(pane2);
  }
}
