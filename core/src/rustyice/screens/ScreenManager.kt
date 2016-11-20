package rustyice.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import rustyengine.RustyEngine
import rustyice.graphics.PerformanceTracker
import java.util.*

/**
 * Manages which screens are currently active and displays them.
 */
class ScreenManager(val tracker: PerformanceTracker? = null){
    val stage: Stage

    private val screens: HashMap<String, LazyScreen>
    private val screenHistory: Stack<Screen>
    private var currentScreen: Screen?

    init {
        stage = Stage(RustyEngine.viewport, RustyEngine.batch)

        currentScreen = null
        screens = HashMap()
        screenHistory = Stack()
    }


    /**
     * adds a screen with a given name
     *
     * @param name the name the screen will be referenced to by
     * @param screen a screen object
     */
    fun addScreen(name: String, screen: () -> Screen) {
        screens.put(name, LazyScreen(screen))
    }

    /**
     * displays a screen, the last screen will be added to a stack.
     *
     * @param name see add screen
     */
    fun showScreen(name: String) {
        val screen = screens[name]
        var currentScreen = currentScreen

        if(screen != null) {
            if(currentScreen != null) {
                currentScreen.hide()
                screenHistory.push(currentScreen)
            }
            currentScreen = screen.get()

            currentScreen.show()
            currentScreen.resize(Gdx.graphics.width, Gdx.graphics.height)
            this.currentScreen = currentScreen


            showTracker()
        } else {
            throw IllegalArgumentException("No screen is registered with the name")
        }
    }

    private fun showTracker() {
        if (tracker != null) {
            stage.actors.removeValue(tracker, true)
            stage.addActor(tracker)
            tracker.setFillParent(true)
            tracker.validate()
        }
    }

    /**
     * sets a screen to the screen before the last activation
     */
    fun popScreen() {
        val popScreen: Screen?

        currentScreen?.hide()
        popScreen = screenHistory.pop()
        currentScreen = popScreen

        if(popScreen != null) {
            popScreen.show()
            showTracker()
        }
    }

    /**
     * NEEDS to be called whenever the window resizes for screens to behave properly.
     *
     * @param width in pixels
     * @param height in pixels
     */
    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        currentScreen?.resize(width, height)

        tracker?.validate()
    }

    /**
     * NEEDS to be called every frame
     *
     * @param delta in seconds
     */
    fun render(batch: Batch, delta: Float) {
        currentScreen?.render(batch, delta)
        stage.act(delta)
        stage.draw()
    }

    fun dispose() {
        stage.dispose()
    }

    private inner class LazyScreen(private val builder: () -> Screen){
        private var screen: Screen? = null

        fun get(): Screen {
            val screen = screen

            if(screen == null){
                val out = builder()
                out.screenManager = this@ScreenManager
                this.screen = out
                return out
            } else {
                return screen
            }
        }
    }
}
