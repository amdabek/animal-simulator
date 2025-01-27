package org.pon1645.ooprojekt.model;

import org.pon1645.ooprojekt.SimulationConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genes {
    private final int genomeLength;
    private final List<Integer> genes;

    public Genes(SimulationConfig config) {
        this.genomeLength = config.genomeLength;
        this.genes = new ArrayList<>(genomeLength);
        randomizeGenes();
    }

    public Genes(Genes other) {
        this.genomeLength = other.genomeLength;
        this.genes = new ArrayList<>(other.genes);
    }

    public Genes(Genes a, Genes b, double ratio, SimulationConfig config) {
        this.genomeLength = config.genomeLength;
        this.genes = new ArrayList<>(genomeLength);
        Random r = new Random();
        boolean stronger = (ratio >= 0.5);
        int w = (int) Math.round(genomeLength * Math.min(ratio, 1 - ratio));
        int s = genomeLength - w;
        boolean left = r.nextBoolean();
        if (stronger) {
            if (left) {
                genes.addAll(a.genes.subList(0, s));
                genes.addAll(b.genes.subList(s, genomeLength));
            } else {
                genes.addAll(b.genes.subList(0, w));
                genes.addAll(a.genes.subList(w, genomeLength));
            }
        } else {
            if (left) {
                genes.addAll(b.genes.subList(0, s));
                genes.addAll(a.genes.subList(s, genomeLength));
            } else {
                genes.addAll(a.genes.subList(0, w));
                genes.addAll(b.genes.subList(w, genomeLength));
            }
        }
    }

    private void randomizeGenes(){
        Random rand = new Random();
        for(int i=0; i<genomeLength; i++){
            genes.add(rand.nextInt(8));
        }
    }

    public void mutate(SimulationConfig config){
        Random rand = new Random();
        int minMut = config.minMutation;
        int maxMut = config.maxMutation;
        int howMany = rand.nextInt(maxMut - minMut + 1) + minMut;
        for (int i=0; i<howMany; i++){
            int idx = rand.nextInt(genomeLength);
            switch (config.mutationVariant){
                case FULL_RANDOM -> {
                    int oldVal = genes.get(idx);
                    int newVal;
                    do { newVal = rand.nextInt(8); }
                    while(newVal == oldVal);
                    genes.set(idx, newVal);
                }
                case LIGHT_CORRECTION -> {
                    int oldVal = genes.get(idx);
                    int sign = rand.nextBoolean() ? 1 : -1;
                    int newVal = (oldVal + sign + 8) % 8;
                    genes.set(idx, newVal);
                }
            }
        }
    }

//    public int getRandomGene(){
//        Random rand = new Random();
//        return genes.get(rand.nextInt(genomeLength));
//    }

    @Override
    public String toString(){
        return genes.toString();
    }

    public int size() {
        return genomeLength;
    }

    public int getGene(int idx) {
        return genes.get(idx);
    }
}
