package rustyice.screens

import aurelienribon.tweenengine.TweenManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage

/**
 * The temple for any screen, add it to the ScreenManager.
 * 
 * @author gabek
 * @see ScreenManager
 */
abstract class Screen {
    lateinit var screenManager: ScreenManager
        internal set
    val stage: Stage get() = screenManager.stage
    val tweenManager: TweenManager get() = screenManager.tweenManager

    /**
     * Called when the screen is signaled to appear.
     * @param stage
     */
    abstract fun show()

    /**
     * 
     * @param stage
     */
    abstract fun hide()

    abstract fun dispose()

    abstract fun resize(width: Int, height: Int)

    abstract fun render(batch: Batch, delta: Float)
}
