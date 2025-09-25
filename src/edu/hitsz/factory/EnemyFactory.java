package edu.hitsz.factory;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.MobEnemy;

import java.util.LinkedList;
import java.util.List;

public interface EnemyFactory {
    public abstract AbstractAircraft createEnemy();    
}
