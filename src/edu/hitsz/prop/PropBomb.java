package edu.hitsz.prop;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;

import java.util.ArrayList;
import java.util.List;

public class PropBomb extends BaseProp{
    private List<AbstractFlyingObject> observersFlying = new ArrayList<>();

    public PropBomb(int locationX, int locationY, int speedX, int speedY){
        super(locationX, locationY, speedX, speedY);
    }
    
    @Override    
    public void action(){
        System.out.println("BombSupply active!");
        notifyObservers();
    }

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
