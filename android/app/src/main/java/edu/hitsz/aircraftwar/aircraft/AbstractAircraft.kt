package edu.hitsz.aircraftwar.aircraft

import edu.hitsz.aircraftwar.basic.AbstractFlyingObject
import edu.hitsz.aircraftwar.bullet.BaseBullet

/**
 * Abstract parent class for all types of aircraft:
 * Enemy (BOSS, ELITE, MOB), Hero aircraft
 */
abstract class AbstractAircraft(
    locationX: Int,
    locationY: Int,
    speedX: Int,
    speedY: Int,
    hp: Int
) : AbstractFlyingObject(locationX, locationY, speedX, speedY) {

    /** Max hit points */
    protected val maxHp: Int = hp

    /** Current hit points */
    var hp: Int = hp

    fun decreaseHp(decrease: Int) {
        hp -= decrease
        if (hp <= 0) {
            hp = 0
            vanish()
        }
    }

    /**
     * Aircraft shooting method.
     * Shootable objects must implement this.
     * @return list of bullets shot, or empty list if non-shootable
     */
    abstract fun shoot(): List<BaseBullet>
}
