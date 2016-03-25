package gken.rustyice.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.esotericsoftware.kryo.Kryo;

import gken.rustyice.editor.EditorScreen;
import gken.rustyice.game.Game;
import gken.rustyice.resources.Resources;
import gken.rustyice.screens.GameDisplayScreen;
import gken.rustyice.screens.MainMenu;
import gken.rustyice.screens.ScreenManager;
import gken.rustyice.screens.SettingsScreen;

public class GdxMain implements ApplicationListener{

	private Game game;
	private SpriteBatch batch;
	private ScreenManager screenManager;
	private Resources resources;
	
	private GeneralSettings settings;
	private FileHandle settingsFile;
	
	public GdxMain(GeneralSettings settings, FileHandle settingsFile){
		this.settings = settings;
		this.settingsFile = settingsFile;
	}
	
	@Override
	public void create() {
		Box2D.init();
		resources = new Resources();
		resources.loadAll();
		
		batch = new SpriteBatch();
		
		screenManager = new ScreenManager(batch, resources);
		Gdx.input.setInputProcessor(screenManager.getStage());
		
		game = new Game();
		
		
		screenManager.addScreen("main_menu", new MainMenu());
		screenManager.addScreen("settings", new SettingsScreen(settings, settingsFile));
		screenManager.addScreen("playing", new GameDisplayScreen(game));
		screenManager.addScreen("editor", new EditorScreen());
		
		screenManager.showScreen("playing");
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
}