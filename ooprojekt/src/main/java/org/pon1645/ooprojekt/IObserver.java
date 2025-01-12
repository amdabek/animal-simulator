package org.pon1645.ooprojekt;

public interface IObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object element);
}
