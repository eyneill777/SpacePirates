package rustyice.game.physics.components;

import com.badlogic.gdx.math.MathUtils;
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
import rustyice.graphics.RenderFlags;

/**
 * @author gabek
 */
public class DoorPhysicsComponent implements PhysicsComponent{
    private transient boolean initialized = false;
    private transient Tile parent;
    private transient Body sensorBody, doorLeftBody;
    private transient PrismaticJoint jointLeft;

    private float percentOpen;
    private float targetPercentOpen;
    private float moterSpeed = 5;

    private float clock = 0;

    @Override
    public float getX() {
        return parent.getX();
    }

    @Override
    public float getY() {
        return parent.getY();
    }

    @Override
    public float getRotation() {
        return parent.getRotation();
    }

    @Override
    public void setX(float x) {}

    @Override
    public void setY(float y) {}

    @Override
    public void setPosition(float x, float y) {}

    @Override
    public void setRotation(float rotation) {}

    @Override
    public void init(GameObject parent) {
        if(!initialized){
            this.parent = (Tile) parent;

            BodyDef sensorBodyDef = new BodyDef();
            sensorBodyDef.type = BodyDef.BodyType.StaticBody;
            sensorBodyDef.angle = MathUtils.degRad * getRotation();
            sensorBodyDef.position.set(parent.getX() + TileMap.TILE_SIZE/2, parent.getY() + TileMap.TILE_SIZE/2);
            sensorBody = parent.getSection().getWorld().createBody(sensorBodyDef);

            FixtureDef sensorFixtureDef = new FixtureDef();
            sensorFixtureDef.isSensor = true;
            sensorFixtureDef.filter.categoryBits = FillterFlags.ACTIVATABLE;
            sensorFixtureDef.filter.maskBits = FillterFlags.ACTIVATOR;

            PolygonShape polyShape = new PolygonShape();
            polyShape.setAsBox(TileMap.TILE_SIZE/2, TileMap.TILE_SIZE/2);
            sensorFixtureDef.shape = polyShape;
            sensorBody.createFixture(sensorFixtureDef);
            polyShape.dispose();

            doorLeftBody = createDoorPanelBody();
            createDoorPanelOuter(doorLeftBody);
            createDoorPanelInner(doorLeftBody);

            jointLeft = createHingJoint(sensorBody, doorLeftBody);

            initialized = true;
        }
    }

    private Body createDoorPanelBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.angle = MathUtils.degRad * getRotation();
        bodyDef.position.set(parent.getX() + TileMap.TILE_SIZE/2, parent.getY() + TileMap.TILE_SIZE/2);

        return parent.getSection().getWorld().createBody(bodyDef);
    }

    private Fixture createDoorPanelOuter(Body parentBody){
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(TileMap.TILE_SIZE/2, TileMap.TILE_SIZE/8);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = FillterFlags.LARGE | FillterFlags.OPAQUE;
        fixtureDef.filter.maskBits = FillterFlags.LARGE | FillterFlags.SMALL | FillterFlags.LIGHT;
        Fixture fixture = parentBody.createFixture(fixtureDef);
        polygonShape.dispose();

        return fixture;
    }

    private Fixture createDoorPanelInner(Body parentBody){
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(new Vector2(-TileMap.TILE_SIZE/2, 0), new Vector2(TileMap.TILE_SIZE/2, 0));

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = edgeShape;
        fixtureDef.filter.categoryBits = FillterFlags.WALL;
        fixtureDef.filter.maskBits = FillterFlags.CAMERA_POV;
        Fixture fixture = parentBody.createFixture(fixtureDef);
        edgeShape.dispose();

        return fixture;
    }

    public PrismaticJoint createHingJoint(Body base, Body door){
        PrismaticJointDef jointDef = new PrismaticJointDef();

        jointDef.bodyA = base;
        jointDef.bodyB = door;

        jointDef.enableLimit = true;
        jointDef.upperTranslation = 0;
        jointDef.lowerTranslation = -TileMap.TILE_SIZE;

        jointDef.maxMotorForce = 5;
        jointDef.enableMotor = true;
        //jointDef.motorSpeed = 1;
        //jointDef.maxMotorForce = 1;
        return (PrismaticJoint) parent.getSection().getWorld().createJoint(jointDef);
    }

    public float getPercentOpen() {
        return percentOpen;
    }

    public float getTargetPercentOpen() {
        return targetPercentOpen;
    }

    public void setTargetPercentOpen(float targetPercentOpen) {
        this.targetPercentOpen = targetPercentOpen;
    }

    @Override
    public void store() {
        if(initialized){
            parent.getSection().getWorld().destroyJoint(jointLeft);

            parent.getSection().getWorld().destroyBody(sensorBody);
            parent.getSection().getWorld().destroyBody(doorLeftBody);
            initialized = false;
        }
    }

    @Override
    public void update(float delta) {
        percentOpen = jointLeft.getJointTranslation() / jointLeft.getLowerLimit();
        if(Math.abs(percentOpen - targetPercentOpen) < 0.05f) {
            jointLeft.setMotorSpeed(0);
        } else{
            if(percentOpen > targetPercentOpen){
                jointLeft.setMotorSpeed(moterSpeed);
            } else {
                jointLeft.setMotorSpeed(-moterSpeed);
            }
        }


        clock += delta;
        targetPercentOpen = ((int)(clock) % 2 == 0)?0:1;
    }

    @Override
    public void beginCollision(Collision collision) {

    }

    @Override
    public void endCollision(Collision collision) {

    }
}
