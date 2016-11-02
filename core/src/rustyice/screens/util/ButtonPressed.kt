package rustyice.screens.util

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class ButtonPressed(val callback: () -> Unit): ClickListener() {
    override fun clicked(event: InputEvent?, x: Float, y: Float) {
        super.clicked(event, x, y)
        callback()
    }
}