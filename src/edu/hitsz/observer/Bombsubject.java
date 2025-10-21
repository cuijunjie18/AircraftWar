package edu.hitsz.observer;

import java.util.ArrayList;
import java.util.List;

import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.EnemyBullet;

public class Bombsubject implements Subject{
    private List<AbstractFlyingObject> observersFlying = new ArrayList<>();

    public void registerObserver(AbstractFlyingObject object){
        observersFlying.add(object);
    }

    public void removeObserver(AbstractFlyingObject object){
        observersFlying.remove(object);
    }

    public void notifyObservers(){
        for (AbstractFlyingObject object : observersFlying){
            object.update();
        }
    }
}
