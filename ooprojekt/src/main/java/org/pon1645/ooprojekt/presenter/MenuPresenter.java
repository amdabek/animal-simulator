package org.pon1645.ooprojekt.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.pon1645.ooprojekt.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuPresenter implements Initializable {
    public TextField height;
    public TextField width;
    public RadioButton equatorial;
    public ToggleGroup plants;
    public RadioButton jungle;
    public ToggleGroup mutation;
    public TextField plantNumber;
    public TextField energyPerMeal;
    public TextField plantsPerDay;
    public TextField animals;
    public TextField energy;
    public TextField fullEnergy;
    public TextField reproduceEnergy;
    public TextField minMutations;
    public TextField maxMutations;
    public TextField genomeLength;
    public Label message;

    public void onSaveButtonClick(ActionEvent actionEvent) {
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation " + "simulationEngine.size()");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    private void setError(String text) {
        message.setText(text);
        message.setStyle("-fx-text-fill: red");
    }

    private void setSuccess(String text) {
        message.setText(text);
        message.setStyle("-fx-text-fill: green");
    }

    public void onStartButtonClick(ActionEvent actionEvent) {
        Stage newSimulation = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
        BorderPane viewRoot = null;
        try {
            viewRoot = loader.load();
        } catch (IOException e) {
            setError(e.getMessage());
            return;
        }
        SimulationPresenter presenter = loader.getController();
        SimulationConfig config = new SimulationConfig();
        config.width = Integer.parseInt(width.getText());
        config.height = Integer.parseInt(height.getText());
        config.plantsPerDay = Integer.parseInt(plantsPerDay.getText());
        config.reproductionEnergyFraction = Float.parseFloat(reproduceEnergy.getText());
        config.startEnergy = Integer.parseInt(energy.getText());
        config.mutationVariant = mutation.getSelectedToggle().getUserData().toString().equals("cautious") ? MutationVariant.LIGHT_CORRECTION : MutationVariant.FULL_RANDOM;
        GlobeMap map = new GlobeMap(config);

        map.spawnGrassAt(new Vector2d(2,2));

        SimulationEngine engine = new SimulationEngine(map);

        Animal a1 = new Animal(map, new Vector2d(2,2));
        Animal a2 = new Animal(map, new Vector2d(2,2));
        Animal a3 = new Animal(map, new Vector2d(0,3));

        engine.addAnimal(a1);
        engine.addAnimal(a2);
        engine.addAnimal(a3);

        engine.run(6);
        presenter.drawMap();
        configureStage(newSimulation, viewRoot);
        newSimulation.show();
        setSuccess("Uruchomiono symulację");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}