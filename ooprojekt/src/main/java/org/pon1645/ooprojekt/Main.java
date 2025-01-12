package org.pon1645.ooprojekt;

public class Main {
    public static void main(String[] args) {
        // Konfiguracja
        SimulationConfig config = new SimulationConfig();
        config.width = 6;
        config.height = 4;
        config.plantsPerDay = 1;
        config.reproductionEnergyFraction = 0.25;
        config.startEnergy = 20;
        //wariant mutacji
        config.mutationVariant = MutationVariant.FULL_RANDOM;

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
    }
}
