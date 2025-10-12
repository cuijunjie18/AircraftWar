package edu.hitsz.strategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.bullet.BaseBullet;

public class NoneShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(
        int x, 
        int y, 
        int speedX, 
        int speedY, 
        int power, 
        int bulletType
    )
    {
        return new LinkedList<>();
    }
    
}
