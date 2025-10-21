package edu.hitsz.observer;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;


public abstract interface Subject {
    public void registerObserver(AbstractFlyingObject object);

    public void removeObserver(AbstractFlyingObject object);

    public void notifyObservers();
}
