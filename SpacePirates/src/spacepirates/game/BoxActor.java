package spacepirates.game;

import com.badlogic.gdx.math.MathUtils;
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
		
		shape.dispose();
	}

	@Override
	public void update(float delta){
		setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getWidth()/2);
	}
	
	@Override
	public void remove(){
		getGame().getWorld().destroyBody(body);
	}
	
	public void move(float x, float y){
		float diffX = x - body.getLinearVelocity().x;
		float diffY = y - body.getLinearVelocity().y;
		if(Math.abs(diffX) > .01 || Math.abs(diffY) > .01){
			body.applyLinearImpulse(diffX*3, diffY*3, body.getWorldCenter().x, body.getWorldCenter().y, true);
		}
	}
	
	protected BodyDef buildBodyDef(){
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = .9f;
		
		bodyDef.position.x = getX();
		bodyDef.position.y = getY();
		
		return bodyDef;
	}
	
	protected FixtureDef buildFixtureDef(){
		FixtureDef fixtureDef = new FixtureDef();
		
		fixtureDef.density = 10;
		
		return fixtureDef;
	}
	
	protected abstract Shape buildShape();
}
