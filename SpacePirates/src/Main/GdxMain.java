package Main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ResourceLoader.Resources;
import Screens.MainMenu;
import Screens.ScreenManager;

public class GdxMain implements ApplicationListener{

	private SpriteBatch batch;
	private ScreenManager screenManager;
	private Resources resources;
	
	@Override
	public void create() {
		resources = new Resources();
		resources.loadAll();
		
		batch = new SpriteBatch();
		
		screenManager = new ScreenManager(batch, resources);
		Gdx.input.setInputProcessor(screenManager.getStage());
		
		screenManager.addScreen("main_menu", new MainMenu());
		screenManager.showScreen("main_menu");
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //clear screen
		
		float delta = Gdx.graphics.getDeltaTime();
		
		screenManager.render(batch, delta);
	}

	@Override
	public void resize(int width, int height) {
		 screenManager.resize(width, height);
	}

	@Override
	public void dispose() {
		batch.dispose();
		screenManager.dispose();
	}
	
	@Override
	public void resume() {} //don't need
	
	@Override
	public void pause() {} //don't need
	
	public static void main(String[] args){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 800;
		//config.fullscreen=true;
		
		config.title = "test";
		
		
		new LwjglApplication(new GdxMain(), config);
	}
}
