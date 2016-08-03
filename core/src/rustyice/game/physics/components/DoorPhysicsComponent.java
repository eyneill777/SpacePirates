package rustyice.game.physics.components;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import rustyice.game.GameObject;
import rustyice.game.Section;
import rustyice.game.physics.Collision;
import rustyice.game.tiles.Tile;

/**
 * @author gabek
 */
public class DoorPhysicsComponent implements PhysicsComponent{
    private transient boolean initialized = false;
    private transient Body sensorBody, doorLeftBody, doorRightBody;
    private transient Fixture sensorFixture, doorLeftFixture, doorRightFixture, doorLeftInternal, doorRightInternal;
    private transient PrismaticJoint jointLeft, jointRight;
    private float x;
    private float y;

    @Deprecated
    public DoorPhysicsComponent(){
    }

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
        PolygonShape polyShape = new PolygonShape();

        BodyDef sensorBodyDef = new BodyDef();
        sensorBodyDef.type = BodyDef.BodyType.StaticBody;

        sensorBody = parent.getSection().getWorld().createBody(sensorBodyDef);

        FixtureDef sensorFixtureDef = new FixtureDef();
        sensorFixtureDef.isSensor = true;

        polyShape.setAsBox(0.1f, 0.5f);
        sensorFixtureDef.shape = polyShape;
        sensorFixture = sensorBody.createFixture(sensorFixtureDef);

        //PrismaticJointDef jointDef = new PrismaticJointDef();


        polyShape.dispose();

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
