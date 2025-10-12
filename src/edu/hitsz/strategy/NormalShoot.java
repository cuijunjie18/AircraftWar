package edu.hitsz.strategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import edu.hitsz.utils.Utils;

public class NormalShoot implements ShootStrategy {
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
        List<BaseBullet> res = new LinkedList<>();
        BaseBullet bullet;
        bullet = Utils.getBullet(x, y, speedX, speedY, power, bulletType);
        res.add(bullet);
        return res;
    }
}
