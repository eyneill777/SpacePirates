package rustyice.physics

import rustyice.game.GameObject
import rustyice.game.tiles.Tile

abstract class TilePhysicsComponent: PhysicsComponent() {
    @Transient
    var parentTile: Tile? = null
    override var parent: GameObject?
        get() = parentTile
        set(value){
            if(value is Tile){
                parentTile = value
            } else {
                throw IllegalArgumentException("Parent must be of the Tile type.")
            }
        }
}