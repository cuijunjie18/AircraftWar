package edu.hitsz.factory;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.PropBulletPlus;

public class PropBulletPlusFactory implements PropFactory {
    @Override
    public BaseProp createProp(int x,int y) {
        int speedX = (Math.random() > 0.5) ? 3 : -3; // 随机水平速度方向
        int speedY = 5;
        return new PropBulletPlus(
                x,y,
                speedX,
                speedY 
        );
    }
}
