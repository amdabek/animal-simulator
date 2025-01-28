package org.ponmaj.ooprojekt;

import org.ponmaj.ooprojekt.model.Animal;
import org.ponmaj.ooprojekt.model.GlobeMap;
import org.ponmaj.ooprojekt.model.MutationVariant;
import org.ponmaj.ooprojekt.model.PlantGrowthVariant;

public class Main {
    public static void main(String[] args) {
        ProjektApp.main(args);
//        SimulationConfig config = new SimulationConfig();
//        config.width = 10;
//        config.height = 10;
//        config.initialAnimals = 5;
//        config.initialPlants = 5;
//        config.startEnergy = 20;
//        config.plantEnergy = 10;
//        config.plantsPerDay = 2;
//        config.minEnergyToReproduce = 15;
//        config.reproductionEnergyFraction = 0.25;
//        config.minMutation = 1;
//        config.maxMutation = 3;
//        config.mutationVariant = MutationVariant.FULL_RANDOM;
//        config.plantGrowthVariant = PlantGrowthVariant.EQUATOR;
//        config.genomeLength = 8;
//
//        GlobeMap map = new GlobeMap(config);
//        SimulationEngine engine = new SimulationEngine(map);
//        engine.generateInitialPlantsAndAnimals();
//
//        try {
//            int daysToSimulate = 10;
//            engine.run(daysToSimulate);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Symulacja zakończona.");
//
//        for (Animal animal : engine.getAnimals()) {
//            String status = animal.isDead() ? " (martwy)" : " (żywy)";
//            System.out.println(
//                    "Zwierzę na pozycji " + animal.getPosition() + status +
//                            ", energia = " + animal.getEnergy() +
//                            ", genom = " + animal.getGenes().toString()
//            );
//        }
    }
}
