package rustyice.screens

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.kotcrab.vis.ui.widget.VisProgressBar
import rustyengine.RustyEngine
import rustyengine.resources.Resources

class LoadingScreen: Screen(){
    private val root: Container<VisProgressBar>
    private val progressBar: VisProgressBar
    
    init {
        progressBar = VisProgressBar(0f, 1f, 0.01f, false)
        root = Container(progressBar)
        root.setFillParent(true)
    }

    override fun show() {
        stage.addActor(root)
    }

    override fun hide() {
        stage.actors.removeValue(root, true)
    }

    override fun dispose() {}

    override fun resize(width: Int, height: Int) {}

    override fun render(batch: Batch, delta: Float) {
        if(RustyEngine.resorces.update()){
            RustyEngine.resorces.loadAll()
            screenManager.showScreen("main_menu")
        }
        
        progressBar.value = RustyEngine.resorces.progress
    }
}