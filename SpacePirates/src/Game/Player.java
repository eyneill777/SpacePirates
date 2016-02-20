package Game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import input.PlayerInput;

public class Player extends Actor{
	private PlayerInput playerInput;
	private Sprite boxSprite;
	
	@Override
	public void update(float delta) {
		
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
		
		setWidth(20);
		setHeight(20);
	}

	@Override
	public void remove() {
		
	}
}
