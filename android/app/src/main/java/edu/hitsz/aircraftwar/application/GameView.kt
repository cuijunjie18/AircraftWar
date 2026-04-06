package edu.hitsz.aircraftwar.application

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import edu.hitsz.aircraftwar.aircraft.AbstractAircraft
import edu.hitsz.aircraftwar.aircraft.HeroAircraft
import edu.hitsz.aircraftwar.aircraft.MobEnemy
import edu.hitsz.aircraftwar.basic.AbstractFlyingObject
import edu.hitsz.aircraftwar.bullet.BaseBullet
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Game main view, handles game logic and rendering.
 * Uses SurfaceView for high-performance drawing on Android.
 */
class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback, Runnable {

    private var backgroundTop = 0

    /** Time interval (ms), controls refresh rate */
    private val timeInterval = 40

    private lateinit var heroAircraft: HeroAircraft
    private val enemyAircrafts: MutableList<AbstractAircraft> = CopyOnWriteArrayList()
    private val heroBullets: MutableList<BaseBullet> = CopyOnWriteArrayList()
    private val enemyBullets: MutableList<BaseBullet> = CopyOnWriteArrayList()

    /** Max number of enemy aircraft on screen */
    private val enemyMaxNumber = 5

    /** Current score */
    private var score = 0

    /** Current time */
    private var time = 0

    /** Cycle duration (ms) - controls bullet firing and enemy spawn frequency */
    private val cycleDuration = 600
    private var cycleTime = 0

    /** Game over flag */
    private var gameOverFlag = false

    /** Game thread */
    private var gameThread: Thread? = null
    private var isRunning = false

    /** Paint objects for drawing */
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = 48f
        typeface = Typeface.DEFAULT_BOLD
    }

    /** Scaled background image */
    private var scaledBackground: Bitmap? = null

    /** Game over callback */
    var onGameOver: ((Int) -> Unit)? = null

    init {
        holder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Set screen dimensions for game objects
        AbstractFlyingObject.screenWidth = width
        AbstractFlyingObject.screenHeight = height

        // Scale background image to fit screen
        scaledBackground = Bitmap.createScaledBitmap(
            ImageManager.backgroundImage, width, height, true
        )

        // Initialize hero aircraft
        heroAircraft = HeroAircraft(
            width / 2,
            height - ImageManager.heroImage.height,
            0, 0, 100
        )

        // Start game loop
        isRunning = true
        gameThread = Thread(this)
        gameThread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        AbstractFlyingObject.screenWidth = width
        AbstractFlyingObject.screenHeight = height
        scaledBackground = Bitmap.createScaledBitmap(
            ImageManager.backgroundImage, width, height, true
        )
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isRunning = false
        try {
            gameThread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        while (isRunning && !gameOverFlag) {
            val startTime = System.currentTimeMillis()

            time += timeInterval

            // Periodic execution (frequency control)
            if (timeCountAndNewCycleJudge()) {
                // Spawn new enemy aircraft
                if (enemyAircrafts.size < enemyMaxNumber) {
                    val mobWidth = ImageManager.mobEnemyImage.width
                    enemyAircrafts.add(
                        MobEnemy(
                            (Math.random() * (width - mobWidth)).toInt(),
                            (Math.random() * height * 0.05).toInt(),
                            0,
                            10,
                            30
                        )
                    )
                }
                // Aircraft shoot bullets
                shootAction()
            }

            // Move bullets
            bulletsMoveAction()

            // Move aircraft
            aircraftsMoveAction()

            // Collision detection
            crashCheckAction()

            // Post-processing
            postProcessAction()

            // Draw frame
            drawFrame()

            // Check if hero is alive
            if (heroAircraft.hp <= 0) {
                gameOverFlag = true
                post { onGameOver?.invoke(score) }
            }

            // Frame rate control
            val elapsed = System.currentTimeMillis() - startTime
            val sleepTime = timeInterval - elapsed
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    // =====================
    //   Action methods
    // =====================

    private fun timeCountAndNewCycleJudge(): Boolean {
        cycleTime += timeInterval
        if (cycleTime >= cycleDuration) {
            cycleTime %= cycleDuration
            return true
        }
        return false
    }

    private fun shootAction() {
        // TODO: Enemy shooting

        // Hero shooting
        heroBullets.addAll(heroAircraft.shoot())
    }

    private fun bulletsMoveAction() {
        for (bullet in heroBullets) {
            bullet.forward()
        }
        for (bullet in enemyBullets) {
            bullet.forward()
        }
    }

    private fun aircraftsMoveAction() {
        for (enemyAircraft in enemyAircrafts) {
            enemyAircraft.forward()
        }
    }

    /**
     * Collision detection:
     * 1. Enemy attacks hero
     * 2. Hero attacks/collides with enemy
     * 3. Hero picks up supplies
     */
    private fun crashCheckAction() {
        // TODO: Enemy bullets attack hero

        // Hero bullets attack enemy
        for (bullet in heroBullets) {
            if (bullet.notValid()) continue
            for (enemyAircraft in enemyAircrafts) {
                if (enemyAircraft.notValid()) continue
                if (enemyAircraft.crash(bullet)) {
                    // Enemy hit by hero bullet
                    enemyAircraft.decreaseHp(bullet.getPower())
                    bullet.vanish()
                    if (enemyAircraft.notValid()) {
                        // TODO: Score and supply drops
                        score += 10
                    }
                }
                // Hero and enemy collision - both destroyed
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish()
                    heroAircraft.decreaseHp(Int.MAX_VALUE)
                }
            }
        }

        // TODO: Hero picks up supplies
    }

    /**
     * Post-processing:
     * 1. Remove invalid bullets
     * 2. Remove invalid enemy aircraft
     */
    private fun postProcessAction() {
        enemyBullets.removeAll { it.notValid() }
        heroBullets.removeAll { it.notValid() }
        enemyAircrafts.removeAll { it.notValid() }
    }

    // =====================
    //   Drawing methods
    // =====================

    private fun drawFrame() {
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas()
            if (canvas != null) {
                drawGame(canvas)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (canvas != null) {
                try {
                    holder.unlockCanvasAndPost(canvas)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun drawGame(canvas: Canvas) {
        // Draw scrolling background
        scaledBackground?.let { bg ->
            canvas.drawBitmap(bg, 0f, (backgroundTop - height).toFloat(), paint)
            canvas.drawBitmap(bg, 0f, backgroundTop.toFloat(), paint)
            backgroundTop += 1
            if (backgroundTop >= height) {
                backgroundTop = 0
            }
        }

        // Draw bullets first, then aircraft (bullets appear below aircraft)
        drawObjects(canvas, enemyBullets)
        drawObjects(canvas, heroBullets)
        drawObjects(canvas, enemyAircrafts)

        // Draw hero aircraft
        val heroImg = ImageManager.heroImage
        canvas.drawBitmap(
            heroImg,
            (heroAircraft.getLocationXVal() - heroImg.width / 2).toFloat(),
            (heroAircraft.getLocationYVal() - heroImg.height / 2).toFloat(),
            paint
        )

        // Draw score and life
        drawScoreAndLife(canvas)
    }

    private fun drawObjects(canvas: Canvas, objects: List<AbstractFlyingObject>) {
        for (obj in objects) {
            val image = obj.getImageBitmap() ?: continue
            canvas.drawBitmap(
                image,
                (obj.getLocationXVal() - image.width / 2).toFloat(),
                (obj.getLocationYVal() - image.height / 2).toFloat(),
                paint
            )
        }
    }

    private fun drawScoreAndLife(canvas: Canvas) {
        val x = 20f
        var y = 60f
        canvas.drawText("SCORE: $score", x, y, textPaint)
        y += 50f
        canvas.drawText("LIFE: ${heroAircraft.hp}", x, y, textPaint)
    }

    // =====================
    //   Touch control
    // =====================

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                if (x < 0 || x > width || y < 0 || y > height) {
                    return true
                }
                if (::heroAircraft.isInitialized) {
                    heroAircraft.setLocation(x.toDouble(), y.toDouble())
                }
            }
        }
        return true
    }

    /**
     * Stop the game
     */
    fun stopGame() {
        isRunning = false
    }
}
