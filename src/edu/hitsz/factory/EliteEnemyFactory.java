package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteEnemyFactory implements EnemyFactory{
    @Override
    public AbstractAircraft createEnemy() {
        return new EliteEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                            (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                            3,
                            10,
                            60); // 更高的血量与拥有水平速度
    }
    
}
