package spacepirates.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

import spacepirates.input.Actions;
import spacepirates.input.PlayerInput;

public class Player extends BoxActor{
	private PlayerInput playerInput;
	private Sprite boxSprite;
	private float speed = 6;
	
	public Player() {
		super();
		setWidth(1);
		setHeight(1);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		float fx = 0, fy = 0;
		if(playerInput.isPressed(Actions.MOVE_UP)){
			fy += speed;
		}
		if(playerInput.isPressed(Actions.MOVE_DOWN)){
			fy -= speed;
		}
		if(playerInput.isPressed(Actions.MOVE_LEFT)){
			fx -= speed;
		}
		if(playerInput.isPressed(Actions.MOVE_RIGHT)){
			fx += speed;
		}
		
		walk(fx, fy, 1);
		
		if(getCollisions().size() > 0){
			boxSprite.setColor(Color.BLUE);
		} else {
			boxSprite.setColor(Color.GREEN);
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		boxSprite.setX(getX());
		boxSprite.setY(getY());
		
		boxSprite.draw(batch);
	}

	@Override
	public void init() {
		super.init();
		playerInput = getGame().getPlayerInput();
		boxSprite = new Sprite(getResources().circle);
		
		boxSprite.setSize(getWidth(), getHeight());
		boxSprite.setOrigin(getWidth()/2, getHeight()/2);
	}

	@Override
	public void store() {
		super.store();
	}

	@Override
	protected FixtureDef buildFixtureDef() {
		FixtureDef fixtureDef = super.buildFixtureDef();
		fixtureDef.filter.categoryBits = Game.CAT_AGENT;
		fixtureDef.filter.maskBits = ~Game.CAT_OPEN_PORTEL;
		
		return fixtureDef;
	}
	
	@Override
	protected void buildFixtures(Body body) {
		Shape shape = buildShape();
		FixtureDef fixDef = buildFixtureDef();
		fixDef.shape = shape;
		
		body.createFixture(fixDef);
		
		shape.dispose();
	}
	
	private Shape buildShape() {
		CircleShape shape = new CircleShape();
		
		shape.setRadius(getWidth()/2);
		
		return shape;
	}
}
