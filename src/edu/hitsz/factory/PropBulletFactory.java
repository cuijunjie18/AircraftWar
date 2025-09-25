package edu.hitsz.factory;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.PropBullet;

public class PropBulletFactory implements PropFactory {
    @Override
    public BaseProp createProp(int x,int y) {
        return new PropBullet(
                x,y,
                3, // speedX
                10 // speedY
        );
    }
}
