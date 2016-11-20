package rustyice.game.tiles

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import rustyengine.RustyEngine
import rustyice.game.physics.RectWallComponent
import rustyengine.resources.Resources

class GlassTile() : Tile(true, false) {

    override fun init() {
        super.init()

        val sprite = Sprite(RustyEngine.resorces.box)
        sprite.color = Color(0f, 0.1f, 0.1f, 0.5f)

        this.sprite = sprite
    }

    init {
        tilePhysics = RectWallComponent()
    }
}
