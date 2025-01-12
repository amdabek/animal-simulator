package org.pon1645.ooprojekt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genes {
    public static final int GENOME_LENGTH = 32;
    private final List<Integer> genes;

    // Konstruktor losowy
    public Genes() {
        this.genes = new ArrayList<>(GENOME_LENGTH);
        randomizeGenes();
    }

    // Kopiowanie genomu
    public Genes(Genes other) {
        this.genes = new ArrayList<>(other.genes);
    }


    public Genes(Genes parentA, Genes parentB, double ratio) {
        this.genes = new ArrayList<>(GENOME_LENGTH);
        Random rand = new Random();

        boolean aIsStronger = (ratio >= 0.5);

        // cutPoint = liczba genów od "słabszego" rodzica
        int weakerPart = (int) Math.round(GENOME_LENGTH * Math.min(ratio, 1 - ratio));
        int strongerPart = GENOME_LENGTH - weakerPart;

        // true => silniejszy rodzic daje "lewą" część
        // false => silniejszy rodzic daje "prawą" część
        boolean strongerParentLeftSide = rand.nextBoolean();

        if (aIsStronger) {
            if (strongerParentLeftSide) {
                this.genes.addAll(parentA.genes.subList(0, strongerPart));
                this.genes.addAll(parentB.genes.subList(strongerPart, GENOME_LENGTH));
            } else {
                this.genes.addAll(parentB.genes.subList(0, weakerPart));
                this.genes.addAll(parentA.genes.subList(weakerPart, GENOME_LENGTH));
            }
        } else {
            // parentB jest silniejszy (lub =0.5)
            if (strongerParentLeftSide) {
                // B daje lewą część, A daje prawą
                this.genes.addAll(parentB.genes.subList(0, strongerPart));
                this.genes.addAll(parentA.genes.subList(strongerPart, GENOME_LENGTH));
            } else {
                // B daje prawą część, A daje lewą
                this.genes.addAll(parentA.genes.subList(0, weakerPart));
                this.genes.addAll(parentB.genes.subList(weakerPart, GENOME_LENGTH));
            }
        }
    }

    // Losowe geny
    private void randomizeGenes() {
        Random rand = new Random();
        genes.clear();
        for (int i = 0; i < GENOME_LENGTH; i++) {
            genes.add(rand.nextInt(8));
        }
    }

    // Mutacja wybranej liczby genów
    public void mutate(MutationVariant variant) {
        int minMut = 1;
        int maxMut = 4;
        Random rand = new Random();
        int numGenesToMutate = rand.nextInt(maxMut - minMut + 1) + minMut;

        for (int i = 0; i < numGenesToMutate; i++) {
            int index = rand.nextInt(GENOME_LENGTH);
            switch (variant) {
                case FULL_RANDOM -> {
                    int oldVal = genes.get(index);
                    int newVal;
                    do {
                        newVal = rand.nextInt(8);
                    } while (newVal == oldVal);
                    genes.set(index, newVal);
                }
                case LIGHT_CORRECTION -> {
                    int oldVal = genes.get(index);
                    int sign = rand.nextBoolean() ? 1 : -1;
                    int newVal = (oldVal + sign + 8) % 8;
                    genes.set(index, newVal);
                }
            }
        }
    }

    // Zwraca losowy gen
    public int getRandomGene() {
        Random rand = new Random();
        return genes.get(rand.nextInt(GENOME_LENGTH));
    }

    @Override
    public String toString() {
        return genes.toString();
    }
}
