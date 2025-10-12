package edu.hitsz.strategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import edu.hitsz.utils.Utils;

public class WaveShoot implements ShootStrategy {
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
        int shootNum = 20; // 固定20颗子弹
        List<BaseBullet> res = new LinkedList<>();


        // 发射20颗子弹，分布在180度（π弧度）范围内：从 0 到 π
        for (int i = 0; i < shootNum; i++) {
            // 角度从 0 到 π 均匀分布
            double angle = Math.PI * i / (shootNum - 1); // 当 shootNum > 1

            int speedX_divide;
            int speedY_divide;

            // 计算速度分量（屏幕坐标系：y 向下为正）
            if (bulletType == 1){
                speedX_divide = (int) Math.round(5 * Math.cos(angle));
                speedY_divide = (int) Math.round(5 * Math.sin(angle));
            } else { // hero bullet
                speedX_divide = (int) Math.round(10 * Math.cos(angle));
                speedY_divide = (int) Math.round(-10 * Math.sin(angle));
            }

            BaseBullet bullet = Utils.getBullet(x, y, speedX_divide, speedY_divide, power, bulletType);
            res.add(bullet);
        }
        return res;
    }
}
