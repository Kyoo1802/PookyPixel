package com.kyoo.pixel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kyoo.pixel.inject.GuiceFXMLLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainApp extends Application {
    private final Injector injector = Guice.createInjector(new SceneModule());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        log.debug("Starting your application0.");
        log.warn("Starting your application1.");
        log.info("Starting your application2.");

        GuiceFXMLLoader loader = new GuiceFXMLLoader(injector);

        Parent root = loader.load(getClass().getResource("views/scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("stylesheets/style.css").toExternalForm());

        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }

}