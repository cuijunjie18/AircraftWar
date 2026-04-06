package edu.hitsz.aircraftwar.basic

import android.graphics.Bitmap
import edu.hitsz.aircraftwar.aircraft.AbstractAircraft
import edu.hitsz.aircraftwar.application.ImageManager

/**
 * Base class for all flying objects
 */
abstract class AbstractFlyingObject {

    // locationX, locationY are the center coordinates of the image
    /** x-axis coordinate */
    protected var locationX: Int = 0

    /** y-axis coordinate */
    protected var locationY: Int = 0

    /** x-axis movement speed */
    protected var speedX: Int = 0

    /** y-axis movement speed */
    protected var speedY: Int = 0

    /** Image bitmap, null means not set */
    protected var image: Bitmap? = null

    /** x-axis length, obtained from image size. -1 means not set */
    protected var width: Int = -1

    /** y-axis length, obtained from image size. -1 means not set */
    protected var height: Int = -1

    /** Valid (alive) flag. Objects marked false will be cleared on next refresh */
    protected var isValid: Boolean = true

    /** Screen width, set at runtime */
    companion object {
        var screenWidth: Int = 512
        var screenHeight: Int = 768
    }

    constructor()

    constructor(locationX: Int, locationY: Int, speedX: Int, speedY: Int) {
        this.locationX = locationX
        this.locationY = locationY
        this.speedX = speedX
        this.speedY = speedY
    }

    /**
     * Move the flying object based on speed.
     * If the object touches the horizontal boundary, reverse horizontal speed.
     */
    open fun forward() {
        locationX += speedX
        locationY += speedY
        if (locationX <= 0 || locationX >= screenWidth) {
            // Reverse direction when exceeding horizontal boundary
            speedX = -speedX
        }
    }

    /**
     * Collision detection: when the other object's coordinates enter our range,
     * it is determined that we are hit.
     * Overlap of areas is considered a collision.
     *
     * Non-aircraft object area:
     *   Horizontal: [x - width/2, x + width/2]
     *   Vertical: [y - height/2, y + height/2]
     *
     * Aircraft object area:
     *   Horizontal: [x - width/2, x + width/2]
     *   Vertical: [y - height/4, y + height/4]
     *
     * @param flyingObject the other object
     * @return true if we are hit; false otherwise
     */
    fun crash(flyingObject: AbstractFlyingObject): Boolean {
        // Scale factor to control y-axis area range
        val factor = if (this is AbstractAircraft) 2 else 1 // our side
        val fFactor = if (flyingObject is AbstractAircraft) 2 else 1 // other side

        val x = flyingObject.getLocationXVal()
        val y = flyingObject.getLocationYVal()
        val fWidth = flyingObject.getWidthVal()
        val fHeight = flyingObject.getHeightVal()

        return x + (fWidth + this.getWidthVal()) / 2 > locationX
                && x - (fWidth + this.getWidthVal()) / 2 < locationX
                && y + (fHeight / fFactor + this.getHeightVal() / factor) / 2 > locationY
                && y - (fHeight / fFactor + this.getHeightVal() / factor) / 2 < locationY
    }

    fun getLocationXVal(): Int = locationX

    fun getLocationYVal(): Int = locationY

    fun setLocation(locationX: Double, locationY: Double) {
        this.locationX = locationX.toInt()
        this.locationY = locationY.toInt()
    }

    fun getSpeedYVal(): Int = speedY

    fun getImageBitmap(): Bitmap? {
        if (image == null) {
            image = ImageManager.get(this)
        }
        return image
    }

    fun getWidthVal(): Int {
        if (width == -1) {
            val img = ImageManager.get(this)
            if (img != null) {
                width = img.width
            }
        }
        return width
    }

    fun getHeightVal(): Int {
        if (height == -1) {
            val img = ImageManager.get(this)
            if (img != null) {
                height = img.height
            }
        }
        return height
    }

    fun notValid(): Boolean = !isValid

    /**
     * Mark as vanished. isValid = false, notValid() => true.
     */
    fun vanish() {
        isValid = false
    }
}
