package rustyice.game.tiles

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import rustyice.game.physics.RectWallComponent
import rustyice.resources.Resources

class GlassTile: Tile {
    constructor(): super(true, false){
        tilePhysics = RectWallComponent()
    }

    override fun init() {
        super.init()

        val sprite = Sprite(Resources.box)
        sprite.color = Color(0f, 0.1f, 0.1f, 0.5f)

        this.sprite = sprite
    }
}
