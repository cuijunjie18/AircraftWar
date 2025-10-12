package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class SuperEliteEnemy extends AbstractAircraft{
    /**攻击方式 */
    private String shootMode = "SCATTER"; // 默认散射

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向下发射：1，向上发射：-1)
     */
    private int direction = 1;

    private int shoot_interval = 3; // 射击间隔
    private int shoot_count = 0; // 射击计数


    public SuperEliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setShootStrategy(shootMode);
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
        if (shoot_count % shoot_interval != 0) { // 控制射击频率
            return new LinkedList<>();
        }
        shoot_count = 0;
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = this.getSpeedX();
        int speedY = this.getSpeedY() + direction*2;
        return shootStrategy.shoot(x, y, speedX, speedY, power, 1);
    }
}
