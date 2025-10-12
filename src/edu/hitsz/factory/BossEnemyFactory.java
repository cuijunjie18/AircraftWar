package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossEnemyFactory implements EnemyFactory{
    @Override
    public AbstractAircraft createEnemy() {
        int speedX = (Math.random() > 0.5) ? 2 : -2; // 随机水平速度方向
        int speedY = 0;
        int hp = 120;
        return new BossEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth())),
                            (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                            speedX,
                            speedY,
                            hp); // 更高的血量与拥有水平速度
    }
    
}
