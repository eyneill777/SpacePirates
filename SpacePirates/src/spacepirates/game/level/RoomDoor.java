package spacepirates.game.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;

import spacepirates.game.BoxActor;
import spacepirates.game.Game;

public class RoomDoor extends BoxActor{
	private Sprite sensorSprite, lockSprite;
	private Fixture sensor, lock;
	
	public RoomDoor(float x, float y){
		setPosition(3, 3);
		setSize(.5f, 2);
		setRotation(90);
	}
	
	@Override
	public void init(){
		super.init();
		
		sensorSprite = new Sprite(getResources().box);
		sensorSprite.setBounds(getX()+getWidth()/2, getY(), getWidth()/2, getHeight());
		sensorSprite.setOrigin(0, getHeight()/2);
		sensorSprite.setColor(Color.ORANGE);
		sensorSprite.setRotation(getRotation());
		
		lockSprite = new Sprite(getResources().box);
		lockSprite.setBounds(getX(), getY(), getWidth()/2, getHeight());
		lockSprite.setOrigin(getWidth()/2, getHeight()/2);
		lockSprite.setColor(Color.CYAN);
		lockSprite.setRotation(getRotation());
	}
	
	
	private Shape buildSensorShape() {
		PolygonShape sensorShape = new PolygonShape();
		sensorShape.setAsBox(getWidth()/4, getHeight()/2, new Vector2(getWidth()/4, 0), 0);
		return sensorShape;
	}

	private Shape buildLockShape() {
		PolygonShape lockShape = new PolygonShape();
		lockShape.setAsBox(getWidth()/4, getHeight()/2, new Vector2(-getWidth()/4, 0), 0);
		return lockShape;
	}
	
	@Override
	protected BodyDef buildBodyDef(){
		BodyDef bodyDef = super.buildBodyDef();
		
		bodyDef.type = BodyType.StaticBody;
		bodyDef.angle = MathUtils.degRad * getRotation();
		
		return bodyDef;
	}
	
	@Override
	protected FixtureDef buildFixtureDef(){
		FixtureDef fixDef = super.buildFixtureDef();
		
		fixDef.filter.categoryBits = Game.CAT_OPEN_PORTEL;
		
		return fixDef;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		sensorSprite.draw(batch);
		lockSprite.draw(batch);
	}


	@Override
	protected void buildFixtures(Body body) {
		Shape sensorShape = buildSensorShape();
		Shape lockShape = buildLockShape();
		
		FixtureDef sensorDef = new FixtureDef();
		sensorDef.isSensor = true;
		sensorDef.shape = sensorShape;
		sensor = body.createFixture(sensorDef);
		
		FixtureDef lockDef = buildFixtureDef();
		lockDef.shape = lockShape;
		lock = body.createFixture(lockDef);
		
		sensorShape.dispose();
		lockShape.dispose();
	}
	
	@Override
	public void store(){
		super.store();
		sensor = null;
		lock = null;
	}
}
