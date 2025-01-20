package org.pon1645.ooprojekt.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import org.pon1645.ooprojekt.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimulationPresenter implements IObserver {
    public CheckBox debug;
    public GridPane mapGrid;
    public Label animalAmount;
    public Label plantAmount;
    public Label freeSpaces;
    public Label mostPopularGene;
    public Label averageEnergy;
    public Label averageLifespan;
    public Label averageChildren;
    public ImageView selectedImage;
    public Label selectedGenome;
    public Label selectedActiveGenes;
    public Label selectedEnergy;
    public Label selectedGrass;
    public Label selectedChildren;
    public Label selectedRelatives;
    public Label selectedAge;
    public Button pauseButton;
    private Pane selectedPane;
    private SimulationEngine simulation;
    private Animal selectedAnimal;

    private Future<Void> future;
    private ExecutorService executorService;

    private boolean paused = false;

    public void startSimulation(SimulationEngine simulation, ExecutorService executorService) {
        this.simulation = simulation;
        this.executorService = executorService;
        future = executorService.submit(simulation);
        for (Animal animal : simulation.getAnimals())
            animal.addObserver(this);
        update();
   }

    public void drawMap() {
        clearGrid();
        GlobeMap globeMap = simulation.getMap();
        int width = globeMap.getWidth();
        int height = globeMap.getHeight();
        float AREA = 80*13;
        float size = Math.min(AREA/(width+1), AREA/(height+1));
        for (int i = 0; i <= height; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(size));
        }
        for (int i = 0; i <= width; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(size));
        }
        for (int i = 0; i <= height; i++) {
            for (int j = 0; j <= width; j++) {
                Node cell;
                if (i == 0 && j == 0)
                    cell = new Label("y\\x");
                else if (i == 0)
                    cell = new Label(j-1+"");
                else if (j == 0)
                    cell = new Label(i-1+"");
                else {
                    cell = new StackPane();
                    StackPane cellPane = (StackPane) cell;
                    ImageView cellImageView = new ImageView();
                    cellPane.getChildren().add(cellImageView);
                    Vector2d location = new Vector2d(j-1, i-1);
                    String image = "";
                    if (globeMap.hasGrassAt(location))
                        image = "grass.png";
                    if (globeMap.hasAnimalAt(location)) {
                        List<Animal> animals = globeMap.getAnimalsAt(location);
                        if (animals.size() == 1) {
                            Animal animal = animals.getFirst();
                            cellPane.setOnMouseClicked(mouseEvent -> {
                                selectedAnimal = animal;
                                if (selectedPane != null)
                                    selectedPane.setStyle("-fx-border-width: 0px");
                                selectedPane = cellPane;
                                updateSelectedStats();
                            });
                            if (animal.getEnergy() >= globeMap.getConfig().startEnergy*0.75)
                                image = "full.png";
                            else if (animal.getEnergy() <= globeMap.getConfig().startEnergy*0.25) {
                                image = "empty.png";
                            }
                            else image = "tired.png";
                        }
                        else if (animals.size() == 2)
                            image = "love.png";
                        else image = "fight.png";
                        if (debug.isSelected() && animals.stream().anyMatch(animal -> true))
                            cellPane.setStyle("-fx-border-width: 4px; -fx-border-color: red");
                        if (animals.contains(selectedAnimal))
                            selectedPane = cellPane;
                    }
                    cellImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + image))));
                    cellImageView.setFitWidth(size);
                    cellImageView.setFitHeight(size);
                    if (debug.isSelected()) {
                        cellPane.getChildren().add(new Label("Pref.:\n TAK"));
                    }
                }
                mapGrid.add(cell, j, i);
                GridPane.setHalignment(cell, HPos.CENTER);
            }
        }
    }

    //TODO potrzebna implementacja w Animal
    private void updateSelectedStats() {
        if (selectedAnimal != null) {
            selectedPane.setStyle("-fx-border-width: 4px; -fx-border-color: green");
            ImageView imageView = (ImageView) selectedPane.getChildren().getFirst();
            selectedImage.setImage(imageView.getImage());
            selectedGenome.setText(selectedAnimal.getGenes().toString());
            selectedEnergy.setText(String.valueOf(selectedAnimal.getEnergy()));
            selectedAge.setText("0 " + (selectedAnimal.isDead() ? "(Nie żyje)" : ""));
            selectedActiveGenes.setText("0");
            selectedChildren.setText("0");
            selectedAge.setText("0");
            selectedChildren.setText("0");
            selectedGrass.setText("0");
            selectedRelatives.setText("0");
        }
    }

    //TODO potrzebna implementacja w GlobeMap
    private void updateStats() {
        animalAmount.setText(String.valueOf(simulation.getAnimals().size()));
        plantAmount.setText("0");
        mostPopularGene.setText("0");
        freeSpaces.setText("0");
        averageChildren.setText("0");
        averageEnergy.setText("0");
        averageLifespan.setText("0");
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void update() {
        drawMap();
        updateSelectedStats();
        updateStats();
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object element) {
        Platform.runLater(this::update);
    }

    public void onPauseButtonClicked(ActionEvent actionEvent) {
        if (paused) {
            future = executorService.submit(simulation);
            pauseButton.setStyle("-fx-text-fill: green");
        }
        else {
            future.cancel(true);
            pauseButton.setStyle("-fx-text-fill: red");
        }
        paused = !paused;
    }

    public void onDebugPressed(ActionEvent actionEvent) {
        update();
    }
}
