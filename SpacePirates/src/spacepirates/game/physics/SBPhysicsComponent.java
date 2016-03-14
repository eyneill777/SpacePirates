package spacepirates.game.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import spacepirates.game.Actor;

/**
 * @author gabek
 * Single body physics component
 */
public class SBPhysicsComponent implements PhysicsComponent {
    private Body body;
    private boolean flying;
    private float groundFriction = .5f;
    private boolean initialized;
    private float x, y, rotation;
    private Actor master;

    public SBPhysicsComponent(Actor master){
        this.master = master;
    }

    public void init(){
        if(!initialized){
            initialized = true;

            BodyDef bodyDef = buildBodyDef();

            body = master.getGame().getWorld().createBody(bodyDef);
        }
    }

    private void updateDrag(){
        if(initialized && !flying){
            body.setLinearDamping(body.getMass() * groundFriction);
            body.setAngularDamping(body.getMass() * groundFriction);
        }
    }

    public boolean isInitialized(){
        return initialized;
    }

    public void setFlying(boolean flying){
        this.flying = flying;
        if(isInitialized()){
            if(flying){
                body.setLinearDamping(0);
            } else {
                body.setLinearDamping(body.getMass()*groundFriction);
            }
        }
    }

    public void update(float delta){
        rotation = MathUtils.radiansToDegrees * body.getAngle();
        x = body.getPosition().x;// - master.getWidth()/2;
        y = body.getPosition().y;// - master.getHeight()/2;

        if(!flying && Math.abs(body.getLinearVelocity().x) < .1f && Math.abs(body.getLinearVelocity().y) < .1f &&
                !body.getLinearVelocity().isZero()) {
            body.setLinearVelocity(Vector2.Zero);
        }
    }

    @Override
    public void beginCollision(Collision collision) {

    }

    @Override
    public void endCollision(Collision collision) {

    }

    public void store(){
        if(initialized){
            initialized = false;
            master.getGame().getWorld().destroyBody(body);
            body = null;
        }
    }

    public void walk(float maxX, float maxY, float acceleration) {
        float diffX = maxX - body.getLinearVelocity().x;
        float diffY = maxY - body.getLinearVelocity().y;
        if (Math.abs(diffX) > .1f || Math.abs(diffY) > .1f) {
            //cap on force generated.
            if (Math.abs(diffX) > acceleration) {
                diffX = (diffX < 0) ? -acceleration : acceleration;
            }
            if (Math.abs(diffY) > acceleration) {
                diffY = (diffY < 0) ? -acceleration : acceleration;
            }

            body.applyLinearImpulse(diffX, diffY, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    public Body getBody(){
        return body;
    }


    protected BodyDef buildBodyDef(){
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //bodyDef.fixedRotation = true;

        bodyDef.position.x = x;
        bodyDef.position.y = y;
        bodyDef.angle = rotation * MathUtils.degRad;

        return bodyDef;
    }

    @Override
    public boolean shouldCollide(Fixture thisFixture, Fixture otherFixture) {
        return (thisFixture.getFilterData().maskBits & otherFixture.getFilterData().categoryBits) != 0;
    }

    public Fixture addRectangle(float width, float height, float density){
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(width/2, height/2);

        Fixture fix = body.createFixture(rect, density);
        rect.dispose();

        fix.setUserData(master);
        updateDrag();
        return fix;
    }

    public Fixture addCircle(float radius, float density){
        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        Fixture fix = body.createFixture(circle, density);
        circle.dispose();

        fix.setUserData(master);
        updateDrag();
        return fix;
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
        return rotation;
    }

    @Override
    public void setX(float x) {
        this.x = x;
        if(initialized){
            body.setTransform(x, body.getPosition().y, body.getAngle());
        }
    }

    @Override
    public void setY(float y) {
        this.y = y;
        if(initialized){
            body.setTransform(body.getPosition().x, y, body.getAngle());
        }
    }

    @Override
    public void setRotation(float rotation) {
        this.rotation = rotation;
        if(initialized){
            body.setTransform(body.getPosition(), rotation * MathUtils.degRad);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        if(initialized){
            body.setTransform(x, y, body.getAngle());
        }
    }
}
