package org.pon1645.ooprojekt.model;

import org.junit.jupiter.api.Test;
import org.pon1645.ooprojekt.SimulationConfig;

import static org.junit.jupiter.api.Assertions.*;

class GenesTest {

    @Test
    void testGenesCreationRandom() {
        SimulationConfig config = new SimulationConfig();
        config.genomeLength = 10;

        Genes g = new Genes(config);
        assertEquals(10, g.size(), "Rozmiar genomu powinien być równy 10");
    }

    @Test
    void testGenesInheritance() {
        SimulationConfig config = new SimulationConfig();
        config.genomeLength = 6;

        Genes parentA = new Genes(config);
        Genes parentB = new Genes(config);

        Genes child = new Genes(parentA, parentB, 0.5, config);
        assertEquals(6, child.size());
    }

    @Test
    void testMutateFullRandom() {
        SimulationConfig config = new SimulationConfig();
        config.genomeLength = 8;
        config.minMutation = 2;
        config.maxMutation = 4;
        config.mutationVariant = MutationVariant.FULL_RANDOM;

        Genes g = new Genes(config);
        Genes copy = new Genes(g);

        g.mutate(config);

        int differences = 0;
        for (int i = 0; i < g.size(); i++) {
            if (g.getGene(i) != copy.getGene(i)) {
                differences++;
            }
        }
        assertTrue(differences >= 2 && differences <= 4,
                "Powinno zmienić się od 2 do 4 genów w wariancie FULL_RANDOM");
    }

    @Test
    void testMutateLightCorrection() {
        SimulationConfig config = new SimulationConfig();
        config.genomeLength = 8;
        config.minMutation = 1;
        config.maxMutation = 3;
        config.mutationVariant = MutationVariant.LIGHT_CORRECTION;

        Genes g = new Genes(config);
        Genes copy = new Genes(g);

        g.mutate(config);

        int differences = 0;
        for (int i = 0; i < g.size(); i++) {
            int oldVal = copy.getGene(i);
            int newVal = g.getGene(i);
            if (oldVal != newVal) {
                differences++;
                // LIGHT_CORRECTION => newVal == (oldVal +/- 1) mod 8
                int diff = Math.floorMod(newVal - oldVal, 8);
                assertTrue(diff == 1 || diff == 7,
                        "Mutacja LIGHT_CORRECTION powinna zmieniać gen tylko o +/- 1 (mod 8).");
            }
        }
        assertTrue(differences >= 1 && differences <= 3,
                "Powinno zmienić się od 1 do 3 genów w wariancie LIGHT_CORRECTION");
    }

    @Test
    void testGetRandomGene() {
        SimulationConfig config = new SimulationConfig();
        config.genomeLength = 5;
        Genes g = new Genes(config);

        for (int i = 0; i < 20; i++) {
            int gene = g.getRandomGene();
            assertTrue(gene >= 0 && gene < 8, "Wartość genu powinna być w zakresie [0, 7]");
        }
    }

    @Test
    void testToString() {
        SimulationConfig config = new SimulationConfig();
        config.genomeLength = 4;
        Genes g = new Genes(config);

        String s = g.toString();
        assertTrue(s.startsWith("[") && s.endsWith("]"), "toString() powinno zwracać listę w nawiasach kwadratowych");
    }
}

