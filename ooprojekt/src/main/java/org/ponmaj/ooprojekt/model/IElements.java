package org.ponmaj.ooprojekt.model;

public interface IElements {
    Vector2d getPosition();
    boolean isMovable();
    void move(MoveDirection direction);

    default void addObserver(IObserver observer) { }

}
