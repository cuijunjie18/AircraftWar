package edu.hitsz.utils;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;


public final class Utils {
    public static BaseBullet getBullet(int x, int y, int speedX, int speedY, int power, int bulletType){
        BaseBullet bullet = null;
        switch (bulletType) {
            case 0: // 普通英雄子弹
                bullet = new HeroBullet(x, y, speedX, speedY, power); // 发射一颗子弹
                break;
            case 1:
                bullet = new EnemyBullet(x, y, speedX, speedY, power); // 发射一颗子弹
                break;
        }
        return bullet;
    }
}
