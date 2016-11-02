package rustyice.game.tiles

import rustyice.game.physics.DoorPhysicsComponent
import rustyice.physics.PhysicsComponent

/**
 * @author gabek
 */
class DoorTile: Tile {
    constructor(): super(false, true){
        tilePhysics = DoorPhysicsComponent()
    }
}
