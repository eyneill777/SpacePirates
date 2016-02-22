package spacepirates.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	}

	@Override
	public void render(SpriteBatch batch) {
		boxSprite.setX(getX());
		boxSprite.setY(getY());
		boxSprite.setSize(getWidth(), getHeight());
		boxSprite.setOrigin(getWidth()/2, getHeight()/2);
		boxSprite.setRotation(getRotation());
		
		boxSprite.draw(batch);
	}

	@Override
	public void init() {
		super.init();
		playerInput = getGame().getPlayerInput();
		boxSprite = new Sprite(getResources().box);
		
		setWidth(1);
		setHeight(1);
	}

	@Override
	public void remove() {
		super.remove();
	}

	@Override
	protected Shape buildShape() {
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(.5f, .5f);
		
		return boxShape;
	}
}
