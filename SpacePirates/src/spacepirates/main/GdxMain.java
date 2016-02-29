package spacepirates.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.Json;

import spacepirates.game.Game;
import spacepirates.resources.Resources;
import spacepirates.screens.GameDisplayScreen;
import spacepirates.screens.MainMenu;
import spacepirates.screens.ScreenManager;
import spacepirates.screens.SettingsScreen;

public class GdxMain implements ApplicationListener{

	private Game game;
	private SpriteBatch batch;
	private ScreenManager screenManager;
	private Resources resources;
	
	private GeneralSettings settings;
	
	@Override
	public void create() {
		Box2D.init();
		resources = new Resources();
		resources.loadAll();
		
		FileHandle settingsFile = Gdx.files.external("settings.json");
		if(settingsFile.exists()){
			settings = new Json().fromJson(GeneralSettings.class, settingsFile);
		} else {
			settings = new GeneralSettings();
		}
		
		
		batch = new SpriteBatch();
		
		screenManager = new ScreenManager(batch, resources);
		Gdx.input.setInputProcessor(screenManager.getStage());
		
		game = new Game();
		
		
		screenManager.addScreen("main_menu", new MainMenu());
		screenManager.addScreen("settings", new SettingsScreen(settings, settingsFile));
		screenManager.addScreen("playing", new GameDisplayScreen(game));
		
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
		game.dispose();
	}
	
	@Override
	public void resume() {} //don't need
	
	@Override
	public void pause() {} //don't need
	
	public static void main(String[] args){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//config.width = windowSize.width;
		//config.height = windowSize.height;
		//config.fullscreen=true;
		
		//config.width = 800;
		//config.height = 800;
		
		config.addIcon("res/icon.png", FileType.Internal);
		
		config.title = "SP";
		
		
		new LwjglApplication(new GdxMain(), config);
	}
}