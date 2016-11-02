package rustyice.game.tiles

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import rustyice.game.tiles.Tile
import rustyice.resources.Resources

class FloorTile: Tile() {
    override fun init(){
        super.init()

        val sprite = Sprite(Resources.box)
        sprite.color = Color.CORAL

        this.sprite = sprite
    }
}
