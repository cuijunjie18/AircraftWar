package edu.hitsz.factory;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.PropBlood;

public class PropBloodFactory implements PropFactory {
    @Override
    public BaseProp createProp(int x,int y) {
        return new PropBlood(
                x,y,
                3, // speedX
                10 // speedY
        );
    }
}
