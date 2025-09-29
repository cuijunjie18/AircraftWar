package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class MobEnemyFactory implements EnemyFactory{
    @Override
    public AbstractAircraft createEnemy() {
        int speedX = 0;
        int speedY = 10;
        int hp = 30;
        return new MobEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                                speedX,
                                speedY,
                                hp);
    }
}
