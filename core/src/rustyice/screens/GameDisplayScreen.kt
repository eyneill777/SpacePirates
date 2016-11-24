package rustyice.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisWindow
import ktx.actors.onClick
import ktx.vis.KVisTextButton
import ktx.vis.table
import rustyengine.RustyEngine
import rustyice.game.Game
import rustyice.graphics.Camera
import rustyice.graphics.GameDisplay
import rustyice.input.Actions
import rustyice.input.PlayerInput

class GameDisplayScreen() : Screen() {
    private val game: Game = RustyEngine.instance()

    private val display: GameDisplay
    private val camera: Camera
    private val pauseTable: VisTable
    private val pauseWindow: VisWindow
    private val root: Stack
    private val playerInput: PlayerInput
    // private Box2DDebugRenderer debugRender;

    override fun render(batch: Batch, delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            paused = true
        }

        if (!paused) {
            game.update(delta)
            camera.update(delta)
            display.render(batch)
        }
    }

    private var paused: Boolean = false
        set(value){
            field = value

            if(value){
                pauseTable.add(pauseWindow)
                pauseWindow.fadeIn()
            } else {
                pauseWindow.fadeOut()
            }
        }

    override fun show() {
        stage.addActor(root)
        root.pack()
    }

    override fun hide() {
        stage.actors.removeValue(root, true)
    }

    override fun dispose() {
        display.dispose()
    }

    override fun resize(width: Int, height: Int) {
        // this.display.setSize(width, height);
    }

    init {
        display = GameDisplay()
        camera = Camera(12f, 12f)
        display.game = game
        display.camera = camera

        pauseWindow = VisWindow("Pause")
        val pad = 5f
        val prefWidth = 180f
        pauseWindow.add(table{
            textButton("Resume"){
                onClick { inputEvent: InputEvent, kVisTextButton: KVisTextButton ->
                    paused = false
                }
            }.pad(pad).prefWidth(prefWidth).row()

            textButton("Settings"){
                onClick { inputEvent: InputEvent, kVisTextButton: KVisTextButton ->
                    screenManager.showScreen("settings")
                }
            }.pad(pad).prefWidth(prefWidth).row()

            textButton("Exit"){
                onClick { inputEvent: InputEvent, kVisTextButton: KVisTextButton ->
                    screenManager.popScreen()
                }
            }.pad(pad).prefWidth(prefWidth)
        })
        pauseWindow.isMovable = false
        pauseTable = VisTable()

        root = Stack(display, pauseTable)
        root.setFillParent(true)
        playerInput = Actions.desktopDefault()
    }
}