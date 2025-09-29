package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class SuperEliteEnemy extends AbstractAircraft{
    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 3;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向下发射：1，向上发射：-1)
     */
    private int direction = 1;

    private int shoot_interval = 5; // 射击间隔
    private int shoot_count = 0; // 射击计数


    public SuperEliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() { // 实现散射模式
        shoot_count++;
        if (shoot_count % shoot_interval != 0) {
            return new LinkedList<>();
        }
        shoot_count = 0;
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = -2;
        int speedY = this.getSpeedY() + direction*2;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            bullet = new EnemyBullet(x, y, speedX + this.speedX, speedY, power); // 横向速度补偿，提升视觉效果
            res.add(bullet);
            speedX += 2;
        }
        return res;
    }
}
