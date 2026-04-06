package edu.hitsz.aircraftwar.aircraft

import edu.hitsz.aircraftwar.bullet.BaseBullet
import edu.hitsz.aircraftwar.bullet.HeroBullet

/**
 * Hero aircraft, controlled by the player
 */
class HeroAircraft(
    locationX: Int,
    locationY: Int,
    speedX: Int,
    speedY: Int,
    hp: Int
) : AbstractAircraft(locationX, locationY, speedX, speedY, hp) {

    /** Number of bullets fired at once */
    private val shootNum = 1

    /** Bullet damage */
    private val power = 30

    /** Bullet shooting direction (up: -1, down: 1) */
    private val direction = -1

    override fun forward() {
        // Hero aircraft is controlled by touch, does not move via forward()
    }

    /**
     * Produce bullets by shooting
     * @return list of bullets shot
     */
    override fun shoot(): List<BaseBullet> {
        val res = mutableListOf<BaseBullet>()
        val x = this.getLocationXVal()
        val y = this.getLocationYVal() + direction * 2
        val bulletSpeedX = 0
        val bulletSpeedY = this.getSpeedYVal() + direction * 5
        for (i in 0 until shootNum) {
            // Bullet launch position offset relative to aircraft position
            // Multiple bullets spread horizontally
            val bullet = HeroBullet(
                x + (i * 2 - shootNum + 1) * 10,
                y,
                bulletSpeedX,
                bulletSpeedY,
                power
            )
            res.add(bullet)
        }
        return res
    }
}
