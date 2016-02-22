package spacepirates.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
		setWidth(2);
		setHeight(2);
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
		testSprite.setSize(2, 2);
		testSprite.setOrigin(1, 1);
	}

	@Override
	public void remove() {
		super.remove();
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
		
		return fixtureDef;
	}
	
	@Override
	protected Shape buildShape() {
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(1, 1);
		
		return boxShape;
	}

}
