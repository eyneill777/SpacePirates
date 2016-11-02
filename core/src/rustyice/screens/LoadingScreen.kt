package rustyice.screens

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.kotcrab.vis.ui.widget.VisProgressBar
import rustyice.resources.Resources
import rustyice.screens.effects.GuiEffects

class LoadingScreen: Screen{
    private val root: Container<VisProgressBar>
    private val progressBar: VisProgressBar
    
    constructor(): super() {
        progressBar = VisProgressBar(0f, 1f, 0.01f, false)
        root = Container(progressBar)
        root.setFillParent(true)
    }

    override fun show() {
        stage.addActor(root)
    }

    override fun hide() {
        GuiEffects.fadeOut(root, 0.5f, {
            stage.actors.removeValue(root, true)
        }).start(tweenManager)
    }

    override fun dispose() {}

    override fun resize(width: Int, height: Int) {}

    override fun render(batch: Batch, delta: Float) {
        if(Resources.update()){
            Resources.loadAll()
            screenManager.showScreen("main_menu")
        }
        
        progressBar.value = Resources.progress
    }
}