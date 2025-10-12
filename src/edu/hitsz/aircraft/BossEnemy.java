package edu.hitsz.aircraft;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

public class BossEnemy extends AbstractAircraft{
    /**攻击方式 */
    private String shootMode = "WAVE"; // 默认环绕

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向下发射：1，向上发射：-1)
     */
    private int direction = 1;

    private int shoot_interval = 8; // 射击间隔
    private int shoot_count = 7; // 射击计数


    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
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

        int centerX = this.getLocationX();
        int centerY = this.getLocationY();

        return shootStrategy.shoot(centerX, centerY, 0, 0, power, 1);
    }
}

