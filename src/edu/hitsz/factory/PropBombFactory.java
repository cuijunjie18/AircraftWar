package edu.hitsz.factory;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.PropBomb;

public class PropBombFactory implements PropFactory {
    @Override
    public BaseProp createProp(int x,int y) {
        return new PropBomb(
                x,y,
                3, // speedX
                10 // speedY
        );
    }
}
