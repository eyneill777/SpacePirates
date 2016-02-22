package spacepirates.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

public abstract class BoxActor extends Actor{
	private ArrayList<Collision> collisions;
	private boolean flying = false;
	private Body body;
	private Fixture fixture;
	private float groundFriction = .5f;
	
	@Override
	public void init(){
		collisions = new ArrayList<>();
		
		
		Shape shape = buildShape();
		BodyDef bodyDef = buildBodyDef();
		FixtureDef fixtureDef = buildFixtureDef();
		
		body = getGame().getWorld().createBody(bodyDef);
		fixtureDef.shape = shape;
		fixture = body.createFixture(fixtureDef);
		
		body.setUserData(this);
		
		if(!flying){
			body.setLinearDamping(body.getMass()*groundFriction);
		}
		
		shape.dispose();
	}

	public void setFlying(boolean flying){
		this.flying = flying;
		if(body != null){
			if(flying){
				body.setLinearDamping(0);
			} else {
				body.setLinearDamping(body.getMass()*groundFriction);
			}
		}
	}
	
	@Override
	public void update(float delta){
		setRotation(MathUtils.radiansToDegrees * body.getAngle());
		setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getWidth()/2);
	}
	
	@Override
	public void remove(){
		getGame().getWorld().destroyBody(body);
		body = null;
	}
	
	public void walk(float maxX, float maxY, float acceleration){
		float diffX = maxX - body.getLinearVelocity().x;
		float diffY = maxY - body.getLinearVelocity().y;
		if(Math.abs(diffX) > .1f || Math.abs(diffY) > .1f){
			
			//cap on force generated.
			if(Math.abs(diffX) > acceleration){
				diffX = (diffX < 0)?-acceleration:acceleration;
			}
			if(Math.abs(diffY) > acceleration){
				diffY = (diffY < 0)?-acceleration:acceleration;
			}
			
			body.applyLinearImpulse(diffX, diffY, body.getWorldCenter().x, body.getWorldCenter().y, true);
		}
	}
	
	public ArrayList<Collision> getCollisions(){
		return collisions;
	}
	
	@Override
	public void beginCollision(Fixture thisFixture, Fixture otherFixture, Contact contact){
		Collision col = new Collision();
		
		col.thisFixture = thisFixture;
		col.otherFixture = otherFixture;
		col.otherActor = (Actor)otherFixture.getBody().getUserData();
		
		collisions.add(col);
	}
	
	@Override
	public  void endCollision(Fixture thisFixture, Fixture otherFixture, Contact contact){
		for(int i = 0;i < collisions.size(); i++){
			Collision col = collisions.get(i);
			if(col.thisFixture == thisFixture && col.otherFixture == otherFixture){
				//O(1) remove
				collisions.set(i--, collisions.get(collisions.size()-1));
				collisions.remove(collisions.size()-1);
			}
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
