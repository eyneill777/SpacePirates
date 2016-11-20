package rustyice.screens

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage

/**
 * The temple for any screen, add it to the ScreenManager.
 * 
 * @author gabek
 * @see ScreenManager
 */
abstract class Screen {
    lateinit var screenManager: ScreenManager internal set
    val stage: Stage get() = screenManager.stage

    /**
     * Called when the screen is signaled to appear.
     */
    abstract fun show()
    abstract fun hide()

    open fun dispose(){}
    open fun resize(width: Int, height: Int){}
    open fun render(batch: Batch, delta: Float){}
}
