package org.ponmaj.ooprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ponmaj.ooprojekt.presenter.MenuPresenter;

import java.io.IOException;

public class ProjektApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        MenuPresenter presenter = fxmlLoader.getController();
        stage.setTitle("Darwin World");
        stage.setScene(scene);
        stage.setOnHidden(e -> presenter.shutdown());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}