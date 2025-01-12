package org.pon1645.ooprojekt;

public class Grass {
    private final Vector2d position;
    private static final int ENERGY_VALUE = 5;

    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public static int getEnergyValue() {
        return ENERGY_VALUE;
    }

    @Override
    public String toString() {
        return "*";
    }
}
