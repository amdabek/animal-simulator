package org.pon1645.ooprojekt.presenter;

import javafx.application.Platform;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.prefs.Preferences;

public class MenuPresenter implements Initializable {
    public TextField height;
    public TextField width;
    public ToggleGroup plants;
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
    public RadioButton equatorial;
    public RadioButton jungle;
    public RadioButton random;
    public RadioButton cautious;

    private final SimulationConfig config = new SimulationConfig();
    private final Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    private void configureSimulation() {
        config.width = Integer.parseInt(width.getText());
        config.height = Integer.parseInt(height.getText());
        config.plantsPerDay = Integer.parseInt(plantsPerDay.getText());
        config.reproductionEnergyFraction = Double.parseDouble(reproduceEnergy.getText());
        config.startEnergy = Integer.parseInt(energy.getText());
        config.mutationVariant = mutation.getSelectedToggle().getUserData().toString().equals("cautious") ? MutationVariant.LIGHT_CORRECTION : MutationVariant.FULL_RANDOM;
        config.initialAnimals = Integer.parseInt(animals.getText());
        config.initialPlants = Integer.parseInt(plantNumber.getText());
        config.genomeLength = Integer.parseInt(genomeLength.getText());
        config.minMutation = Integer.parseInt(minMutations.getText());
        config.maxMutation = Integer.parseInt(maxMutations.getText());
        config.plantEnergy = Integer.parseInt(energyPerMeal.getText());
        config.plantGrowthVariant = plants.getSelectedToggle().getUserData().toString().equals("jungle") ? PlantGrowthVariant.FOREST_CREEPING : PlantGrowthVariant.EQUATOR;
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
        configureSimulation();
        prefs.putInt("width", config.width);
        prefs.putInt("height", config.height);
        prefs.putInt("plantsPerDay", config.plantsPerDay);
        prefs.putDouble("reproductionEnergyFraction", config.reproductionEnergyFraction);
        prefs.putInt("startEnergy", config.startEnergy);
        prefs.putBoolean("cautious", config.mutationVariant == MutationVariant.LIGHT_CORRECTION);
        prefs.putInt("initialAnimals", config.initialAnimals);
        prefs.putInt("initialPlants", config.initialPlants);
        prefs.putInt("genomeLength", config.genomeLength);
        prefs.putInt("minMutation", config.minMutation);
        prefs.putInt("maxMutation", config.maxMutation);
        prefs.putInt("plantEnergy", config.plantEnergy);
        prefs.putBoolean("jungle", config.plantGrowthVariant == PlantGrowthVariant.FOREST_CREEPING);
        setSuccess("Konfiguracja zapisana");
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
        configureSimulation();
        GlobeMap map = new GlobeMap(config);
        SimulationEngine engine = new SimulationEngine(map);
        engine.generateInitialPlantsAndAnimals();
        configureStage(newSimulation, viewRoot);
        newSimulation.show();
        presenter.startSimulation(engine, executorService);
        setSuccess("Uruchomiono symulację");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        height.setText(prefs.get("height", ""));
        width.setText(prefs.get("width", ""));
        plantsPerDay.setText(prefs.get("plantsPerDay", ""));
        plantNumber.setText(prefs.get("initialPlants", ""));
        energyPerMeal.setText(prefs.get("plantEnergy", ""));
        animals.setText(prefs.get("initialAnimals", ""));
        energy.setText(prefs.get("startEnergy", ""));
        reproduceEnergy.setText(prefs.get("reproductionEnergyFraction", ""));
        minMutations.setText(prefs.get("minMutation", ""));
        maxMutations.setText(prefs.get("maxMutation", ""));
        genomeLength.setText(prefs.get("genomeLength", ""));
        if (prefs.getBoolean("cautious", false))
            cautious.setSelected(true);
        if (prefs.getBoolean("jungle", false))
            jungle.setSelected(true);
    }

    public void shutdown() {
        executorService.shutdownNow();
    }
}