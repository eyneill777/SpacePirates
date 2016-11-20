package rustyice.game.tiles

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import rustyengine.RustyEngine
import rustyice.Core
import rustyice.editor.annotations.ComponentProperty
import rustyice.game.physics.RectWallComponent
import rustyengine.resources.Resources

class WallTile() : Tile(true, true) {

    @ComponentProperty
    var color = Color.BLUE
        set(value) {
            field = value
            sprite?.color = color
        }

    override fun init() {
        super.init()

        val sprite = Sprite(RustyEngine.resorces.box)
        sprite.color = color

        this.sprite = sprite
    }

    init {
        tilePhysics = RectWallComponent()
    }
}
