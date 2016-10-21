package rustyice.game.tiles;

import rustyice.game.physics.DoorPhysicsComponent;

/**
 * @author gabek
 */
public class DoorTile extends Tile {
    private DoorPhysicsComponent physicsComponent;

    public DoorTile(){
        super(false, true);

        setTilePhysics(physicsComponent = new DoorPhysicsComponent());
    }

    @Override
    public void setRotation(float rotation){
        super.setRotation(rotation);
        physicsComponent.store();
        physicsComponent.init(this);
    }
}
