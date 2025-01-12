package org.pon1645.ooprojekt;

import java.util.*;

public class GlobeMap implements IWorldMap {

    private final int width;
    private final int height;
    private final SimulationConfig config;

    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    public GlobeMap(SimulationConfig config) {
        this.width = config.width;
        this.height = config.height;
        this.config = config;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    public SimulationConfig getConfig() {
        return config;
    }

    public boolean hasGrassAt(Vector2d pos) {
        return grasses.containsKey(pos);
    }

    public void removeGrassAt(Vector2d pos) {
        grasses.remove(pos);
    }

    // Możliwe ręczne sadzenie trawy
    public void spawnGrassAt(Vector2d pos) {
        grasses.put(pos, new Grass(pos));
    }
    //warianty trawowe
    public void growPlants() {
        if (config.plantGrowthVariant == PlantGrowthVariant.FOREST_CREEPING) {
            // [F] Pełzająca dżungla
            growPlantsForestCreeping();
        } else {
            // Domyślnie zalesione równiki
            growPlantsEquator();
        }
    }

    private void growPlantsForestCreeping() {
        if (grasses.isEmpty()) {
            for (int i = 0; i < config.plantsPerDay; i++) {
                spawnSinglePlantEquator();
            }
        } else {
            for (int i = 0; i < config.plantsPerDay; i++) {
                spawnPlantNearExisting();
            }
        }
    }

    private void spawnPlantNearExisting() {
        List<Vector2d> existing = new ArrayList<>(grasses.keySet());
        if (existing.isEmpty()) return; // brak roślin do naśladowania

        Random rand = new Random();
        Vector2d chosen = existing.get(rand.nextInt(existing.size()));
        List<Vector2d> neighbors = getNeighbors(chosen);

        List<Vector2d> freeNeighbors = new ArrayList<>();
        for (Vector2d nPos : neighbors) {
            if (!grasses.containsKey(nPos)) {
                freeNeighbors.add(nPos);
            }
        }
        if (!freeNeighbors.isEmpty()) {
            Vector2d spawnPos = freeNeighbors.get(rand.nextInt(freeNeighbors.size()));
            grasses.put(spawnPos, new Grass(spawnPos));
        }
    }

    private List<Vector2d> getNeighbors(Vector2d pos) {
        List<Vector2d> result = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (!(dx == 0 && dy == 0)) {
                    int nx = pos.getX() + dx;
                    int ny = pos.getY() + dy;

                    if (nx < 0) nx = width - 1;
                    if (nx >= width) nx = 0;

                    if (ny >= 0 && ny < height) {
                        result.add(new Vector2d(nx, ny));
                    }
                }
            }
        }
        return result;
    }

    private void growPlantsEquator() {
        for (int i = 0; i < config.plantsPerDay; i++) {
            spawnSinglePlantEquator();
        }
    }

    private void spawnSinglePlantEquator() {
        Random rand = new Random();
        double chance = rand.nextDouble();

        int equatorMin = (int) Math.floor(height * 0.4);
        int equatorMax = (int) Math.ceil(height * 0.6);

        if (chance < 0.8) {
            spawnPlantInEquatorRange(equatorMin, equatorMax);
        } else {
            spawnPlantOutsideEquatorRange(equatorMin, equatorMax);
        }
    }

    private void spawnPlantInEquatorRange(int equatorMin, int equatorMax) {
        Random rand = new Random();
        int maxAttempts = 50;

        while (maxAttempts-- > 0) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(equatorMax - equatorMin) + equatorMin;
            Vector2d pos = new Vector2d(x, y);

            if (!grasses.containsKey(pos)) {
                grasses.put(pos, new Grass(pos));
                return;
            }
        }
    }

    private void spawnPlantOutsideEquatorRange(int equatorMin, int equatorMax) {
        Random rand = new Random();
        int maxAttempts = 50;

        while (maxAttempts-- > 0) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);

            if (y < equatorMin || y >= equatorMax) {
                Vector2d pos = new Vector2d(x, y);
                if (!grasses.containsKey(pos)) {
                    grasses.put(pos, new Grass(pos));
                    return;
                }
            }
        }
    }


    public Vector2d wrapPositionIfNeeded(Vector2d oldPos, Vector2d newPos, Animal animal) {
        int x = newPos.getX();
        int y = newPos.getY();

        // zapętlenie x
        if (x < 0) x = width - 1;
        else if (x >= width) x = 0;

        // blokada y
        if (y < 0 || y >= height) {
            animal.reverseDirection();
            return oldPos;
        }
        return new Vector2d(x, y);
    }
}
