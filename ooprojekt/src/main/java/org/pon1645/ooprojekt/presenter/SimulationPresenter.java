package org.pon1645.ooprojekt.presenter;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.Image;

public class SimulationPresenter {
    public CheckBox debug;
    public GridPane mapGrid;
    public Label message;

    //private WorldMap worldMap;

    //public void setWorldMap(WorldMap worldMap) {
        //this.worldMap = worldMap;
        //this.message.setStyle("-fx-text-fill: black");
        //drawMap();
   //}

    public void drawMap() {
        clearGrid();
        int width = getWidth();
        int height = getHeight();
        float CELL_WIDTH = 50;
        float CELL_HEIGHT = 50;
        for (int i = 0; i <= height; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }
        for (int i = 0; i <= width; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        for (int i = 0; i <= height; i++) {

            for (int j = 0; j <= width; j++) {
                Node cell;
                if (i == 0 && j == 0)
                    cell = new Label("y\\x");
                else if (i == 0)
                    cell = new Label(j+"");
                else if (j == 0)
                    cell = new Label(i+"");
                else {
                    //Object object = null;
                    //WorldElement object = worldMap.objectAt(new Vector2d(j, i));
                    cell = new ImageView(new Image(getClass().getResourceAsStream("/images/up.png")));
                    ImageView cellImageView = (ImageView) cell;
                    cellImageView.setFitWidth(CELL_WIDTH);
                    cellImageView.setFitHeight(CELL_HEIGHT);
                }
                mapGrid.add(cell, j, i);
                GridPane.setHalignment(cell, HPos.CENTER);
            }
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    //@Override
    //public void mapChanged(WorldMap worldMap, String message) {
    //    Platform.runLater(() -> {
    //        drawMap();
    //       this.message.setText(message);
    //    });
    //}

    private int getWidth() {
        return 10; //worldMap.getCurrentBounds().upperRight().getX()-worldMap.getCurrentBounds().lowerLeft().getX();
    }

    private int getHeight() {
        return  10; //worldMap.getCurrentBounds().upperRight().getY()-worldMap.getCurrentBounds().lowerLeft().getY();
    }
}
