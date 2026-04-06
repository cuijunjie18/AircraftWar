package edu.hitsz.aircraftwar.aircraft

import edu.hitsz.aircraftwar.bullet.BaseBullet

/**
 * Mob enemy aircraft.
 * Cannot shoot.
 */
class MobEnemy(
    locationX: Int,
    locationY: Int,
    speedX: Int,
    speedY: Int,
    hp: Int
) : AbstractAircraft(locationX, locationY, speedX, speedY, hp) {

    override fun forward() {
        super.forward()
        // Check if flying out of bounds downward on y-axis
        if (locationY >= screenHeight) {
            vanish()
        }
    }

    override fun shoot(): List<BaseBullet> {
        return emptyList()
    }
}
