package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenu extends Screen{
	private Container<Window> root;
	
	private Texture testTexture;
	private Sprite testSprite;
	
	public MainMenu(){
		//<temp>
		Pixmap pix = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
		pix.setColor(Color.WHITE);
		pix.fill();
		testTexture = new Texture(pix); //gpu
		pix.dispose();
		
		testSprite = new Sprite(testTexture);
		testSprite.setColor(Color.BLUE);
		//</temp>
	}

	@Override
	public void load(Skin skin) {
		Window win = new Window("Main Menu", skin);
		
		TextButton newButt = new TextButton("New Game", skin);
		
		TextButton loadButt = new TextButton("Load Game", skin);
		
		TextButton settingsButt = new TextButton("Settings", skin);
		
		TextButton quitButt = new TextButton("Quit", skin);
		quitButt.addListener(new ChangeListener(){
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
		}});
		
		
		win.add(newButt).pad(10, 10, 5, 10).row();
		win.add(loadButt).pad(5, 10, 5, 10).row();
		win.add(settingsButt).pad(5, 10, 5, 10).row();
		win.add(quitButt).pad(5, 10, 10, 10);
		
		
		root = new Container<>(win);
		root.setFillParent(true);
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		batch.begin();
		
		float speed = 100;
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			testSprite.setY(testSprite.getY() + speed*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			testSprite.setY(testSprite.getY() - speed*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			testSprite.setX(testSprite.getX() - speed*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			testSprite.setX(testSprite.getX() + speed*delta);
		}
		
		testSprite.rotate(1);
		testSprite.draw(batch);
		
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show(Stage stage) {
		stage.addActor(root);
	}
	
	@Override
	public void hide(Stage stage) {
		stage.clear();
	}
	
	@Override
	public void dispose() {
		testTexture.dispose();
	}
}
