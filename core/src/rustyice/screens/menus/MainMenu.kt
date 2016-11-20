package rustyice.screens.menus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.kotcrab.vis.ui.widget.VisTable
import ktx.actors.onClick
import ktx.vis.KVisTextButton
import ktx.vis.table
import rustyice.screens.Screen

/**
 * The menu that greets the player when they start the program.
 *  should contain a splash screen as well as buttons to navigate, maybe an background as well intresting.
 * @author gabek
 * 
 */
class MainMenu() : Screen() {

    /**
     * width of the buttons in the central table.
     */
    private val BUTT_WIDTH = 220f
    private val PAD = 5f
    private val FADE_DURATION = 0.5f
    
    private val root: VisTable

    override fun show() {
        stage.addActor(root)
    }

    override fun hide() {
        //stage.getActors().removeValue(root, true);

        stage.actors.removeValue(root, true)
    }

    init {
        root = table {
            setFillParent(true)
            textButton("New Game"){
                onClick { inputEvent: InputEvent, kVisTextButton: KVisTextButton ->
                    screenManager.showScreen("playing")
                }
            }.pad(PAD).prefWidth(BUTT_WIDTH).row()

            textButton("Load Game"){
                onClick { inputEvent: InputEvent, kVisTextButton: KVisTextButton ->
                    //screenManager.showScreen("")
                }
            }.pad(PAD).prefWidth(BUTT_WIDTH).row()

            textButton("Settings"){
                onClick { inputEvent: InputEvent, kVisTextButton: KVisTextButton ->
                    screenManager.showScreen("settings")
                }
            }.pad(PAD).prefWidth(BUTT_WIDTH).row()

            textButton("Level Edit"){
                onClick { inputEvent: InputEvent, kVisTable: KVisTextButton ->
                    screenManager.showScreen("editor")
                }
            }.pad(PAD).prefWidth(BUTT_WIDTH).row()

            textButton("Quit"){
                onClick { inputEvent: InputEvent, kVisTextButton: KVisTextButton ->
                    Gdx.app.exit()
                }
            }.pad(PAD).prefWidth(BUTT_WIDTH)
        }
    }
}
