package spacepirates.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

public abstract class BoxActor extends Actor{
	private Body body;
	private Fixture fixture;
	
	@Override
	public void init(){
		Shape shape = buildShape();
		BodyDef bodyDef = buildBodyDef();
		FixtureDef fixtureDef = buildFixtureDef();
		
		body = getGame().getWorld().createBody(bodyDef);
		fixtureDef.shape = shape;
		fixture = body.createFixture(fixtureDef);
		
		body.setUserData(this);
		
		body.setLinearDamping(body.getMass()/2);
		
		shape.dispose();
	}

	@Override
	public void update(float delta){
		setRotation(MathUtils.radiansToDegrees * body.getAngle());
		setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getWidth()/2);
		if(Math.abs(body.getLinearVelocity().x) < .1f && Math.abs(body.getLinearVelocity().y) < .1f && 
				!body.getLinearVelocity().isZero()) {
			body.setLinearVelocity(Vector2.Zero);
		}
	}
	
	@Override
	public void remove(){
		getGame().getWorld().destroyBody(body);
	}
	
	public void walk(float maxX, float maxY, float acceleration){
		float diffX = maxX - body.getLinearVelocity().x;
		float diffY = maxY - body.getLinearVelocity().y;
		if(Math.abs(diffX) > .1f || Math.abs(diffY) > .1f){
			if(Math.abs(diffX) > acceleration){
				diffX = (diffX < 0)?-acceleration:acceleration;
			}
			if(Math.abs(diffY) > acceleration){
				diffY = (diffY < 0)?-acceleration:acceleration;
			}
			
			body.applyLinearImpulse(diffX, diffY, body.getWorldCenter().x, body.getWorldCenter().y, true);
		}
	}
	
	protected BodyDef buildBodyDef(){
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		
		bodyDef.position.x = getX();
		bodyDef.position.y = getY();
		
		return bodyDef;
	}
	
	protected FixtureDef buildFixtureDef(){
		FixtureDef fixtureDef = new FixtureDef();
		
		fixtureDef.density = 1;
		fixtureDef.friction = .5f;
		
		return fixtureDef;
	}
	
	protected abstract Shape buildShape();
}
