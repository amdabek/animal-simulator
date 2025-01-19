package org.pon1645.ooprojekt;

public class Main {
    public static void main(String[] args) {
        SimulationConfig config = new SimulationConfig();
        config.width = 10;
        config.height = 10;
        config.initialAnimals = 3;
        config.initialPlants = 5;
        config.startEnergy = 10;
        config.plantEnergy = 4;
        config.plantsPerDay = 1;
        config.genomeLength = 6;
        config.minMutation = 1;
        config.maxMutation = 4;
        config.mutationVariant = MutationVariant.FULL_RANDOM;
        config.plantGrowthVariant = PlantGrowthVariant.EQUATOR;
        config.reproductionEnergyFraction = 0.5;

        GlobeMap map = new GlobeMap(config);
        SimulationEngine engine = new SimulationEngine(map);

        engine.generateInitialPlantsAndAnimals();
        engine.run(10);
    }
}
