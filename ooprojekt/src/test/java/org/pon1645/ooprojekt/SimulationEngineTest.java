package org.pon1645.ooprojekt;

import org.junit.jupiter.api.Test;
import org.pon1645.ooprojekt.model.GlobeMap;
import org.pon1645.ooprojekt.model.MutationVariant;
import org.pon1645.ooprojekt.model.PlantGrowthVariant;


import static org.junit.jupiter.api.Assertions.*;

class SimulationEngineTest {

    @Test
    void testVariantOne_EQUATOR_FULLRANDOM() throws InterruptedException {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;
        config.initialAnimals = 2;
        config.initialPlants = 2;
        config.startEnergy = 30;
        config.plantEnergy = 10;
        config.plantsPerDay = 2;
        config.minEnergyToReproduce = 15;
        config.reproductionEnergyFraction = 0.4;
        config.minMutation = 1;
        config.maxMutation = 3;
        config.mutationVariant = MutationVariant.FULL_RANDOM;
        config.plantGrowthVariant = PlantGrowthVariant.EQUATOR;
        config.genomeLength = 6;

        GlobeMap map = new GlobeMap(config);
        SimulationEngine engine = new SimulationEngine(map);

        engine.generateInitialPlantsAndAnimals();

        assertEquals(2, engine.getAnimals().size());

        for (int i = 0; i < 3; i++) {
            engine.doOneDay();
        }

        long aliveCount = engine.getAnimals().stream().filter(a -> !a.isDead()).count();
        assertTrue(aliveCount >= 1, "Przynajmniej jedno zwierzę powinno przeżyć 3 dni przy startEnergy=30");
    }

    @Test
    void testVariantTwo_FORESTCREEPING_LIGHTCORRECTION() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;
        config.initialAnimals = 2;
        config.initialPlants = 0;
        config.startEnergy = 10;
        config.plantEnergy = 2;
        config.plantsPerDay = 1;
        config.minEnergyToReproduce = 4;
        config.reproductionEnergyFraction = 0.5;
        config.minMutation = 1;
        config.maxMutation = 2;
        config.mutationVariant = MutationVariant.LIGHT_CORRECTION;
        config.plantGrowthVariant = PlantGrowthVariant.FOREST_CREEPING;
        config.genomeLength = 4;

        GlobeMap map = new GlobeMap(config);
        SimulationEngine engine = new SimulationEngine(map);

        engine.generateInitialPlantsAndAnimals();
        assertEquals(2, engine.getAnimals().size());
        assertEquals(0, engine.getMap().getGrasses().size());

        engine.doOneDay();

        int sizeAfterDay1 = engine.getAnimals().size();
        assertTrue(sizeAfterDay1 >= 2);

        for (int i = 0; i < 3; i++) {
            engine.doOneDay();
        }
        long aliveCount = engine.getAnimals().stream().filter(a -> !a.isDead()).count();

        assertTrue(aliveCount > 0, "Nie wszystkie zwierzęta powinny paść w ciągu 4 dni, przy starEnergy=10 i roślinach 2/dzień.");
    }

    @Test
    void testVariantThree_NoReproductionThenAllDie() {
        SimulationConfig config = new SimulationConfig();
        config.width = 5;
        config.height = 5;
        config.initialAnimals = 2;
        config.initialPlants = 0;
        config.startEnergy = 5;
        config.plantEnergy = 1;
        config.plantsPerDay = 0;
        config.minEnergyToReproduce = 10;
        config.reproductionEnergyFraction = 0.3;
        config.minMutation = 1;
        config.maxMutation = 1;
        config.mutationVariant = MutationVariant.LIGHT_CORRECTION;
        config.plantGrowthVariant = PlantGrowthVariant.EQUATOR;
        config.genomeLength = 4;

        GlobeMap map = new GlobeMap(config);
        SimulationEngine engine = new SimulationEngine(map);
        engine.generateInitialPlantsAndAnimals();

        for (int i = 0; i < 5; i++) {
            engine.doOneDay();
        }
        long aliveCount = engine.getAnimals().stream().filter(a -> !a.isDead()).count();
        assertEquals(0, aliveCount, "Przy za małej energii początkowej wszystkie zwierzęta powinny umrzeć do 5 dnia.");
    }

    @Test
    void testVariantFour_AlwaysEatSurvive() {
        SimulationConfig config = new SimulationConfig();
        config.width = 6;
        config.height = 5;
        config.initialAnimals = 5;
        config.initialPlants = 10;
        config.startEnergy = 40;
        config.plantEnergy = 10;
        config.plantsPerDay = 5;
        config.minEnergyToReproduce = 20;
        config.reproductionEnergyFraction = 0.2;
        config.minMutation = 2;
        config.maxMutation = 3;
        config.mutationVariant = MutationVariant.FULL_RANDOM;
        config.plantGrowthVariant = PlantGrowthVariant.FOREST_CREEPING;
        config.genomeLength = 6;

        GlobeMap map = new GlobeMap(config);
        SimulationEngine engine = new SimulationEngine(map);
        engine.generateInitialPlantsAndAnimals();

        int days = 5;
        for (int i = 0; i < days; i++) {
            engine.doOneDay();
        }

        long aliveCount = engine.getAnimals().stream().filter(a -> !a.isDead()).count();
        assertTrue(aliveCount >= 5, "Przynajmniej 5 zwierząt powinno przeżyć 5 dni - mogą być też nowe");

    }
}
