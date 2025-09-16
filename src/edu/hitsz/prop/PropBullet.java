package edu.hitsz.prop;

public class PropBullet extends BaseProp{
    public PropBullet(int locationX, int locationY, int speedX, int speedY){
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void action(){
        System.out.println("FireSupply active!");
    }
}
