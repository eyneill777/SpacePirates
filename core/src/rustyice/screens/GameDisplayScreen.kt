package rustyice.screens

import aurelienribon.tweenengine.Tween
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import rustyice.game.Game
import rustyice.game.actors.Player
import rustyice.graphics.Camera
import rustyice.graphics.GameDisplay
import rustyice.graphics.POV
import rustyice.input.Actions
import rustyice.input.PlayerInput
import rustyice.screens.effects.GuiAccessor
import rustyice.screens.effects.GuiEffects
import rustyice.screens.util.ButtonPressed

class GameDisplayScreen: Screen {
    private val display: GameDisplay
    private val camera: Camera
    private val game: Game
    private val pauseWindow: VisWindow
    private val pauseMenu: Container<VisWindow>
    private val root: VisTable
    private val playerInput: PlayerInput
    // private Box2DDebugRenderer debugRender;

    constructor(game: Game): super() {
        this.game = game

        display = GameDisplay()
        camera = Camera(12f, 12f)
        camera.enableFlag(POV)

        display.game = game
        display.camera = camera

        pauseWindow = VisWindow("Pause")

        val resumeButt = VisTextButton("Resume")
        resumeButt.addListener(ButtonPressed({
                paused = false
        }))

        val settingsButt = VisTextButton("Settings")
        settingsButt.addListener(ButtonPressed({
                screenManager.showScreen("settings")
        }))

        val exitButt = VisTextButton("Exit")
        exitButt.addListener(ButtonPressed({
                screenManager.popScreen()
        }))

        pauseWindow.add(resumeButt).pad(5f).prefWidth(180f).row()
        pauseWindow.add(settingsButt).pad(5f).prefWidth(180f).row()
        pauseWindow.add(exitButt).pad(5f).prefWidth(180f)

        pauseWindow.isMovable = false

        pauseMenu = Container(pauseWindow)
        pauseMenu.setFillParent(true)
        pauseMenu.isVisible = false

        root = VisTable()
        root.add(display).grow()

        root.setFillParent(true)
        playerInput = Actions.desktopDefault()
    }

    override fun render(batch: Batch, delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            paused = true
        }

        if (!paused) {
            game.update(delta)
            camera.update(delta)
            display.render(batch, delta)
        }
    }

    private var paused: Boolean = false
        set(value){
            field = value

            if(value){
                pauseMenu.isVisible =  true
                pauseWindow.color.a = 0f
                Tween.to(pauseWindow, GuiAccessor.ALPHA, 0.5f).target(1f).start(tweenManager)
            } else {
                Tween.to(pauseWindow, GuiAccessor.ALPHA, 0.5f).target(0f).setCallback(
                        { i, baseTween -> pauseMenu.isVisible = false }
                ).start(tweenManager)
            }
        }

    override fun show() {
        stage.addActor(root)
        stage.addActor(pauseMenu)
        
        root.pack()
        
        GuiEffects.fadeIn(root, 0.5f).start(tweenManager)
        GuiEffects.fadeIn(pauseMenu, 0.5f).start(tweenManager)

        game.finishLoadingSection()


        val currentSection = game.currentSection
        if(currentSection != null) {
            currentSection.actors.filter({ actor -> actor is Player }).forEach({ actor ->
                (actor as Player).playerInput = playerInput
                camera.target = actor
                camera.isTracking = true
            })
        }
    }

    override fun hide() {
        GuiEffects.fadeOut(root, 0.5f, {
            stage.actors.removeValue(root, true)
        }).start(tweenManager)
        
        GuiEffects.fadeOut(pauseMenu, 0.5f, {
            stage.actors.removeValue(pauseMenu, true)
        }).start(tweenManager)
    }

    override fun dispose() {
        display.dispose()
    }

    override fun resize(width: Int, height: Int) {
        // this.display.setSize(width, height);
    }
}