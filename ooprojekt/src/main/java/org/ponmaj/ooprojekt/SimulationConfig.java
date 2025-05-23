package org.ponmaj.ooprojekt;

import org.ponmaj.ooprojekt.model.MutationVariant;
import org.ponmaj.ooprojekt.model.PlantGrowthVariant;

public class SimulationConfig {
    public int width;
    public int height;
    public int initialAnimals;
    public int initialPlants;
    public int startEnergy;
    public int plantEnergy;
    public int plantsPerDay;
    public double reproductionEnergyFraction;
    public int minMutation;
    public int maxMutation;
    public MutationVariant mutationVariant;
    public PlantGrowthVariant plantGrowthVariant;
    public int genomeLength;
    public int minEnergyToReproduce;
}
