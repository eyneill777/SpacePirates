package rustyice.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import rustyice.game.GameObject;
import rustyice.game.Section;
import rustyice.game.physics.Collision;
import rustyice.game.physics.FillterFlags;
import rustyice.game.tiles.Tile;
import rustyice.game.tiles.TileMap;

/**
 * @author gabek
 */
public class DoorPhysicsComponent implements PhysicsComponent{
    private transient boolean initialized = false;
    private transient Tile parent;
    private transient Body sensorBody, doorLeftBody, doorRightBody;
    private transient Fixture sensorFixture, doorLeftFixture, doorRightFixture, doorLeftInternal, doorRightInternal;
    private transient PrismaticJoint jointLeft, jointRight;
    private float x;
    private float y;

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
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
    public void init(GameObject parent) {
        if(!initialized){
            this.parent = (Tile) parent;
            PolygonShape polyShape = new PolygonShape();

            BodyDef sensorBodyDef = new BodyDef();
            sensorBodyDef.type = BodyDef.BodyType.StaticBody;
            sensorBodyDef.position.set(parent.getX() + TileMap.TILE_SIZE/2, parent.getY() + TileMap.TILE_SIZE/2);
            sensorBody = parent.getSection().getWorld().createBody(sensorBodyDef);

            FixtureDef sensorFixtureDef = new FixtureDef();
            sensorFixtureDef.isSensor = true;
            polyShape.setAsBox(TileMap.TILE_SIZE/2, TileMap.TILE_SIZE/2);
            sensorFixtureDef.shape = polyShape;
            sensorFixture = sensorBody.createFixture(sensorFixtureDef);


            short doorCat = FillterFlags.LARGE;
            short doorMask = FillterFlags.LARGE | FillterFlags.SMALL;

            BodyDef doorLeftBodyDef = new BodyDef();
            doorLeftBodyDef.type = BodyDef.BodyType.DynamicBody;
            doorLeftBodyDef.position.set(parent.getX() + TileMap.TILE_SIZE/4, parent.getY() + TileMap.TILE_SIZE/2);
            doorLeftBody = parent.getSection().getWorld().createBody(doorLeftBodyDef);

            FixtureDef doorLeftFixtureDef = new FixtureDef();
            polyShape.setAsBox(TileMap.TILE_SIZE/4, TileMap.TILE_SIZE/8);
            doorLeftFixtureDef.shape = polyShape;
            doorLeftFixtureDef.filter.maskBits = doorMask;
            doorLeftFixtureDef.filter.categoryBits = doorCat;
            doorLeftFixture = doorLeftBody.createFixture(doorLeftFixtureDef);


            BodyDef doorRightBodyDef = new BodyDef();
            doorRightBodyDef.type = BodyDef.BodyType.DynamicBody;
            doorRightBodyDef.position.set(parent.getX() + TileMap.TILE_SIZE * (3/4f), parent.getY() + TileMap.TILE_SIZE/2);
            doorRightBody = parent.getSection().getWorld().createBody(doorRightBodyDef);

            FixtureDef doorRightFixtureDef = new FixtureDef();
            polyShape.setAsBox(TileMap.TILE_SIZE/4, TileMap.TILE_SIZE/8);
            doorRightFixtureDef.shape = polyShape;
            doorRightFixtureDef.filter.maskBits = doorMask;
            doorRightFixtureDef.filter.categoryBits = doorCat;
            doorRightFixture = doorRightBody.createFixture(doorRightFixtureDef);

            polyShape.dispose();


            PrismaticJointDef jointDef = new PrismaticJointDef();

            jointDef.bodyA = sensorBody;
            jointDef.bodyB = doorRightBody;
            jointDef.enableLimit = true;
            jointDef.upperTranslation = TileMap.TILE_SIZE/4;
            jointLeft = (PrismaticJoint) parent.getSection().getWorld().createJoint(jointDef);

            initialized = true;
        }
    }

    @Override
    public void store() {
        if(initialized){
            parent.getSection().getWorld().destroyJoint(jointLeft);

            parent.getSection().getWorld().destroyBody(sensorBody);
            parent.getSection().getWorld().destroyBody(doorLeftBody);
            parent.getSection().getWorld().destroyBody(doorRightBody);
            initialized = false;
        }
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
