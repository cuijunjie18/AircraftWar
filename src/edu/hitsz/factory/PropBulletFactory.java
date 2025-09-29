package edu.hitsz.factory;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.PropBullet;

public class PropBulletFactory implements PropFactory {
    @Override
    public BaseProp createProp(int x,int y) {
        int speedX = (Math.random() > 0.5) ? 3 : -3; // 随机水平速度方向
        int speedY = 5;
        return new PropBullet(
                x,y,
                speedX,
                speedY 
        );
    }
}
