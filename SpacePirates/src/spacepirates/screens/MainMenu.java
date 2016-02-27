package spacepirates.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenu extends Screen{
	private ShaderProgram backgroundShader;
	private float time = 0;
	private Container<Window> root;
	
	public MainMenu(){
		
	}

	@Override
	public void load(Skin skin) {
		backgroundShader = SimpleBackgroundShader.buildShader();
		
		Window win = new Window("Main Menu", skin);
		
		TextButton newButt = new TextButton("New Game", skin);
		newButt.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				getManager().showScreen("playing");
			}
		});
		
		TextButton loadButt = new TextButton("Load Game", skin);
		
		TextButton settingsButt = new TextButton("Settings", skin);
		
		TextButton quitButt = new TextButton("Quit", skin);
		quitButt.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
		}});
		
		
		win.add(newButt).pad(10, 10, 5, 10).row();
		win.add(loadButt).pad(5, 10, 5, 10).row();
		win.add(settingsButt).pad(5, 10, 5, 10).row();
		win.add(quitButt).pad(5, 10, 10, 10);
		
		win.setMovable(false);
		
		root = new Container<>(win);
		root.setFillParent(true);
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		time += delta;
		
		ShaderProgram oldS = batch.getShader();
		batch.setShader(backgroundShader);
		batch.begin();
		
		backgroundShader.setUniformf("time", time);
		batch.draw(getResources().gameArt.getRegions().first(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		batch.end();
		batch.setShader(oldS);
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
		backgroundShader.dispose();
	}
}
