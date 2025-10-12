package edu.hitsz.strategy;

import java.util.List;

import edu.hitsz.bullet.BaseBullet;

public interface ShootStrategy {
    public abstract List<BaseBullet> shoot(
        int x, 
        int y, 
        int speedX, 
        int speedY, 
        int power, 
        int bulletType
    );
}
