package rustyice.game.tiles

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import rustyice.core.Core
import rustyice.editor.annotations.ComponentProperty
import rustyice.game.physics.RectWallComponent
import rustyice.resources.Resources

class WallTile: Tile {
    constructor(): super(true, true){
        tilePhysics = RectWallComponent()
    }

    var color = Color.BLUE
        @ComponentProperty("Color") get
        @ComponentProperty("Color") set(value) {
            field = value
            sprite?.color = color
        }

    override fun init() {
        super.init()

        val sprite = Sprite(Resources.box)
        sprite.color = color

        this.sprite = sprite
    }
}
