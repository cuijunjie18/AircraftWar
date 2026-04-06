package edu.hitsz.aircraftwar.application

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import edu.hitsz.aircraftwar.R

/**
 * Main Activity - entry point for the Android game
 */
class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen on during gameplay
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Initialize image resources
        ImageManager.init(this)

        // Set content view
        setContentView(R.layout.activity_main)

        // Create and add game view
        gameView = GameView(this)
        val container = findViewById<android.widget.FrameLayout>(R.id.game_container)
        container.addView(gameView)

        // Set game over callback
        gameView.onGameOver = { score ->
            runOnUiThread {
                showGameOverDialog(score)
            }
        }
    }

    private fun showGameOverDialog(score: Int) {
        AlertDialog.Builder(this)
            .setTitle("Game Over!")
            .setMessage("Your score: $score")
            .setPositiveButton("Restart") { _, _ ->
                recreate()
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onPause() {
        super.onPause()
        if (::gameView.isInitialized) {
            gameView.stopGame()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::gameView.isInitialized) {
            gameView.stopGame()
        }
    }
}
