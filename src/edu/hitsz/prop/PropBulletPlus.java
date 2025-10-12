package edu.hitsz.prop;

public class PropBulletPlus extends BaseProp{
    public PropBulletPlus(int locationX, int locationY, int speedX, int speedY){
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void action(){
        System.out.println("FireSupply Plus active!");
    }
}
