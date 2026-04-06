package edu.hitsz.aircraftwar.bullet

import edu.hitsz.aircraftwar.basic.AbstractFlyingObject

/**
 * Base bullet class.
 * Different types of bullets can extend this.
 */
abstract class BaseBullet(
    locationX: Int,
    locationY: Int,
    speedX: Int,
    speedY: Int,
    private val power: Int
) : AbstractFlyingObject(locationX, locationY, speedX, speedY) {

    override fun forward() {
        super.forward()

        // Check x-axis out of bounds
        if (locationX <= 0 || locationX >= screenWidth) {
            vanish()
        }

        // Check y-axis out of bounds
        if (speedY > 0 && locationY >= screenHeight) {
            // Flying downward out of bounds
            vanish()
        } else if (locationY <= 0) {
            // Flying upward out of bounds
            vanish()
        }
    }

    fun getPower(): Int = power
}
