package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TestActor extends Actor{

	private Sprite testSprite;
	
	public TestActor() {
		testSprite = new Sprite();
		
		setWidth(32);
		setHeight(32);
	}
	
	@Override
	public void update(float delta) {
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			setY(getY() + 20*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			setY(getY() - 20*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			setX(getX() - 20*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			setX(getX() + 20*delta);
		}
		
		//setRotation(getRotation() + 1 *delta);
	}

	@Override
	public void render(SpriteBatch batch) {
		testSprite.setX(getX());
		testSprite.setY(getY());
		testSprite.setRotation(getRotation());
		testSprite.draw(batch);
	}

	@Override
	public void init() {
		testSprite = getResources().gameArt.createSprite("may2015-3");
		testSprite.setSize(32, 32);
		testSprite.setOrigin(32/2, 32/2);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
