package rustyice.game.tiles

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import rustyengine.RustyEngine
import rustyice.game.tiles.Tile
import rustyengine.resources.Resources

class FloorTile(): Tile() {
    override fun init(){
        super.init()

        val sprite = Sprite(RustyEngine.resorces.box)
        sprite.color = Color.CORAL

        this.sprite = sprite
    }
}
