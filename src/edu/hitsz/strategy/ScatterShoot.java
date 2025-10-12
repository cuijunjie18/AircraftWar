package edu.hitsz.strategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.bullet.BaseBullet;

import edu.hitsz.utils.Utils;

public class ScatterShoot implements ShootStrategy {
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
        int shootNum = 3; // 固定三连发
        List<BaseBullet> res = new LinkedList<>();
        BaseBullet bullet;
        int speedX_vary = -2; // 初始横向速度
        for(int i=0; i<shootNum; i++){
            bullet = Utils.getBullet(x, y, speedX_vary, speedY, power, bulletType); // 横向速度补偿，提升视觉效果
            res.add(bullet);
            speedX_vary += 2;
        }
        return res;
    }
}
