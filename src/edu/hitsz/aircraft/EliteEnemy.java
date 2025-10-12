package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/*
 * 精英敌机
 * 可以射击
 * 可以掉落道具
 * 
 * @author cjj
 */

public class EliteEnemy extends AbstractAircraft{

    /**攻击方式 */
    private String shootMode = "NORMAL"; // 默认


    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向下发射：1，向上发射：-1)
     */
    private int direction = 1;

    private int shoot_interval = 2; // 射击间隔
    private int shoot_count = 0; // 射击计数


    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setShootStrategy(shootMode); // 设置默认射击策略
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
    public List<BaseBullet> shoot() {
        shoot_count++;
        if (shoot_count % shoot_interval != 0) {
            return new LinkedList<>();
        }
        shoot_count = 0;
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*2;
        return shootStrategy.shoot(x, y, speedX, speedY, power, 1);
    }
}
