package rustyice.game.tiles;

import rustyice.game.physics.components.DoorPhysicsComponent;

/**
 * @author gabek
 */
public class DoorTile extends Tile {
    private DoorPhysicsComponent physicsComponent;

    public DoorTile(){
        super(false, true);

        setTilePhysics(physicsComponent = new DoorPhysicsComponent());
    }
}
