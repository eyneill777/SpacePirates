package rustyice.screens.menus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.vis.KButton
import ktx.vis.KVisTable
import ktx.vis.KVisTextButton
import ktx.vis.table
import rustyice.screens.Screen
import rustyice.screens.effects.ClickSound
import rustyice.screens.effects.GuiEffects
import rustyice.screens.util.ButtonPressed

/**
 * The menu that greets the player when they start the program.
 *  should contain a splash screen as well as buttons to navigate, maybe an background as well intresting.
 * @author gabek
 * 
 */
class MainMenu: Screen {

    /**
     * width of the buttons in the central table.
     */
    private val BUTT_WIDTH = 220f
    private val PAD = 5f
    private val FADE_DURATION = 0.5f
    
    private val root: VisTable

    constructor(){
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

    override fun render(batch: Batch, delta: Float) {
        //batch.setShader(badBackground);
        //batch.begin();

        //batch.draw(getResources().box, 0, 0, 400, 400);
        
        //batch.end();
        //batch.setShader(null);
    }

    override fun resize(width: Int, height: Int) {}

    override fun show() {
        stage.addActor(root)
        GuiEffects.fadeIn(root, FADE_DURATION).start(tweenManager)
    }

    override fun hide() {
        //stage.getActors().removeValue(root, true);
        
        GuiEffects.fadeOut(root, FADE_DURATION, {
            stage.actors.removeValue(root, true)
        }).start(tweenManager)
    }

    override fun dispose(){}
}
