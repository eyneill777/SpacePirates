package spacepirates.game.level;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import spacepirates.game.Collidable;
import spacepirates.game.Game;

public class RoomBoundary implements Collidable{
	private Body body;
	private float x, y, w, h;
	
	public RoomBoundary(float x, float y, float w, float h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public void init(World world){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		
		bodyDef.position.x = x;
		bodyDef.position.y = y;
		
		body = world.createBody(bodyDef);
		body.setUserData(this);
		
		EdgeShape wallShape = new EdgeShape();
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.filter.categoryBits = Game.CAT_BOUNDARY;
		fixtureDef.shape = wallShape;
		
		wallShape.set(0, 0, 0, h);
		body.createFixture(fixtureDef);
		
		wallShape.set(0, h, w, h);
		body.createFixture(fixtureDef);
		
		wallShape.set(w, h, w, 0);
		body.createFixture(fixtureDef);
		
		wallShape.set(w, 0, 0, 0);
		body.createFixture(fixtureDef);
		
		wallShape.dispose();
	}
	
	public void store(World world){
		world.destroyBody(body);
		body = null;
	}
	
	@Override
	public void beginCollision(Fixture thisFixture, Fixture otherFixture, Contact contact) {
		
	}

	@Override
	public void endCollision(Fixture thisFixture, Fixture otherFixture, Contact contact) {
		
	}
}
