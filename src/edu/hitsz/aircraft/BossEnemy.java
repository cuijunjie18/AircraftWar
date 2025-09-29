package edu.hitsz.aircraft;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

public class BossEnemy extends AbstractAircraft{
    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 20;

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

        List<BaseBullet> res = new LinkedList<>();
        int centerX = this.getLocationX();
        int centerY = this.getLocationY();

        // 发射20颗子弹，分布在180度（π弧度）范围内：从 0 到 π
        for (int i = 0; i < shootNum; i++) {
            // 角度从 0 到 π 均匀分布
            double angle = Math.PI * i / (shootNum - 1); // 当 shootNum > 1

            // 计算速度分量（屏幕坐标系：y 向下为正）
            int speedX = (int) Math.round(5 * Math.cos(angle));
            int speedY = (int) Math.round(5 * Math.sin(angle));

            BaseBullet bullet = new EnemyBullet(centerX, centerY, speedX, speedY, power);
            res.add(bullet);
        }

        return res;
    }
}

