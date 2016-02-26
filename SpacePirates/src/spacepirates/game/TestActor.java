package spacepirates.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class TestActor extends BoxActor{

	private Sprite testSprite;
	
	public TestActor() {
		super();
		testSprite = new Sprite();
		
		setPosition(-2, -2);
		setWidth(1.9f);
		setHeight(1.9f);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		//walk(0,1, .95f);
	}

	@Override
	public void render(SpriteBatch batch) {
		testSprite.setX(getX());
		testSprite.setY(getY());
		testSprite.setRotation(270);
		testSprite.draw(batch);
	}

	@Override
	public void init() {
		super.init();
		testSprite = getResources().gameArt.createSprite("may2015-3");
		testSprite.setSize(getWidth(), getHeight());
		testSprite.setOrigin(getWidth()/2, getHeight()/2);
	}

	@Override
	public void store() {
		super.store();
	}

	@Override
	protected BodyDef buildBodyDef(){
		BodyDef bodyDef = super.buildBodyDef();
		//bodyDef.type = BodyType.StaticBody;
		
		return bodyDef;
	}
	
	@Override
	protected FixtureDef buildFixtureDef(){
		FixtureDef fixtureDef = super.buildFixtureDef();
		
		fixtureDef.density = 2;
		fixtureDef.filter.categoryBits = Game.CAT_AGENT;
		
		return fixtureDef;
	}
	
	private Shape buildShape() {
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(getWidth()/2, getHeight()/2);
		
		return boxShape;
	}

	@Override
	protected void buildFixtures(Body body) {
		FixtureDef fixDef = buildFixtureDef();
		Shape shape = buildShape();
		fixDef.shape = shape;
		
		body.createFixture(fixDef);
		
		shape.dispose();
	}

}
