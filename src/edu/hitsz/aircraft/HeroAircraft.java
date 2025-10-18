package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    // 英雄机单例(饿汉式)
    private static final HeroAircraft hero_singleton = 
        new HeroAircraft(Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                0, 0, 1000);

    /**攻击方式 */
    private String shootMode = "NORMAL"; // 默认
    private int shoot_times = 0;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向下发射：1，向上发射：-1)
     */
    private int direction = -1;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    public HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setShootStrategy(shootMode); // 设置默认射击策略
        shoot_times = 0;
    }

    public static HeroAircraft getInstance(){
        return hero_singleton;
    }

    public void changeShootMode(String mode){
        this.shootMode = mode;
        shoot_times = 0; // 重新计数
        setShootStrategy(shootMode); // 切换射击模式
    }

    public void checkShootModeDuration(){
        if(!shootMode.equals("NORMAL")){
            shoot_times++;
            if(shoot_times >= 10){ // 持续200次射击后恢复普通模式
                changeShootMode("NORMAL");
                shoot_times = 0;
            }
        }
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        return shootStrategy.shoot(x, y, speedX, speedY, power, 0);
    }

}
