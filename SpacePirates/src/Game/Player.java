package Game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import input.Actions;
import input.PlayerInput;

public class Player extends Actor{
	private PlayerInput playerInput;
	private Sprite boxSprite;
	private float speed = 20;
	
	@Override
	public void update(float delta) {
		if(playerInput.isPressed(Actions.MOVE_UP)){
			setY(getY() + speed*delta);
		}
		if(playerInput.isPressed(Actions.MOVE_DOWN)){
			setY(getY() - speed*delta);
		}
		if(playerInput.isPressed(Actions.MOVE_LEFT)){
			setX(getX() - speed*delta);
		}
		if(playerInput.isPressed(Actions.MOVE_RIGHT)){
			setX(getX() + speed*delta);
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		boxSprite.setX(getX());
		boxSprite.setY(getY());
		boxSprite.setSize(getWidth(), getHeight());
		
		boxSprite.draw(batch);
	}

	@Override
	public void init() {
		playerInput = getGame().getPlayerInput();
		boxSprite = new Sprite(getResources().box);
		
		setWidth(10);
		setHeight(10);
	}

	@Override
	public void remove() {
		
	}
}
