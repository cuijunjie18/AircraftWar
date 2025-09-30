package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    private HeroAircraft hero;

    @BeforeEach
    void setUp() {
        // 创建一个独立的 HeroAircraft 实例用于测试（不使用单例）
        hero = new HeroAircraft(100, 200, 0, 0, 1000);
    }

    @Test
    void testGetHp_initialValue() {
        assertEquals(1000, hero.getHp());
    }

    @Test
    void testDecreaseHp() {
        // 正常扣血
        hero.decreaseHp(200);
        assertEquals(800, hero.getHp());

        // 扣血超过当前 HP，应归零并调用 vanish()
        hero.decreaseHp(1000);
        assertEquals(0, hero.getHp());
        // 注意：vanish() 是父类方法，通常设置 alive = false，但 AbstractFlyingObject 未提供 isAlive()
        // 若需验证 vanish 行为，需在父类暴露状态。此处仅验证 hp == 0
    }

    @Test
    void testShoot_default() {
        List<BaseBullet> bullets = hero.shoot();

        // 默认 shootNum = 1
        assertEquals(1, bullets.size());

        BaseBullet bullet = bullets.get(0);
        assertTrue(bullet instanceof HeroBullet);

        // 验证位置：x 应为 100（hero x） + (0*2 -1 +1)*10 = 100
        // y 应为 200 + (-1)*2 = 198
        assertEquals(100, bullet.getLocationX());
        assertEquals(198, bullet.getLocationY());

        // 验证速度：speedY = 0 + (-1)*5 = -5
        assertEquals(0, bullet.getSpeedX());
        assertEquals(-5, bullet.getSpeedY());

        // 验证伤害
        // 注意：HeroBullet 的 power 无法直接 get，但可通过设计或反射验证
        // 假设 HeroBullet 有 getPower() 方法（若没有，需补充或跳过）
        // 此处假设 HeroBullet 继承 BaseBullet 且有对应字段
        // 若无法获取，可跳过 power 验证，或通过其他方式（如 mock）
    }

//    @Test
//    void testShoot_multipleBullets() throws Exception {
//        // 通过反射修改 shootNum（因为它是 private）
//        java.lang.reflect.Field shootNumField = HeroAircraft.class.getDeclaredField("shootNum");
//        shootNumField.setAccessible(true);
//        shootNumField.set(hero, 3);
//
//        List<BaseBullet> bullets = hero.shoot();
//        assertEquals(3, bullets.size());
//
//        // 验证横向分散：x 偏移应为 (i*2 - 3 + 1)*10 = (2i - 2)*10
//        // i=0: -20 → x=80
//        // i=1: 0  → x=100
//        // i=2: 20 → x=120
//        assertEquals(80, bullets.get(0).getLocationX());
//        assertEquals(100, bullets.get(1).getLocationX());
//        assertEquals(120, bullets.get(2).getLocationX());
//
//        // 所有 y 相同
//        assertEquals(198, bullets.get(0).getLocationY());
//        assertEquals(198, bullets.get(1).getLocationY());
//        assertEquals(198, bullets.get(2).getLocationY());
//    }
}