package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TestActor extends Actor{

	private Texture testTexture;
	private Sprite testSprite;
	
	public TestActor() {
		Pixmap pix = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
		pix.setColor(Color.WHITE);
		pix.fill();
		testTexture = new Texture(pix); //gpu
		pix.dispose();
		
		testSprite = new Sprite(testTexture);
		testSprite.setColor(Color.BLUE);
		
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
	}

	@Override
	public void render(SpriteBatch batch) {
		testSprite.setX(getX());
		testSprite.setY(getY());
		testSprite.draw(batch);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
