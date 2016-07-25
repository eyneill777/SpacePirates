package rustyice.game.physics.components;

import rustyice.game.physics.Collision;
import rustyice.game.tiles.Tile;

/**
 * @author gabek
 */
public class DoorPhysicsComponent implements PhysicsComponent{
    private Tile master;

    @Deprecated
    public DoorPhysicsComponent(){}

    public DoorPhysicsComponent(Tile master){
        this.master = master;
    }

    @Override
    public float getX() {
        return master.getX();
    }

    @Override
    public float getY() {
        return master.getY();
    }

    @Override
    public float getRotation() {
        return 0;
    }

    @Override
    public void setX(float x) {

    }

    @Override
    public void setY(float y) {

    }

    @Override
    public void setRotation(float rotation) {

    }

    @Override
    public void setPosition(float x, float y) {

    }

    @Override
    public void init() {

    }

    @Override
    public void store() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void beginCollision(Collision collision) {

    }

    @Override
    public void endCollision(Collision collision) {

    }
}
