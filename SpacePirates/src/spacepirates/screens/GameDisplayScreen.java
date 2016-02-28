package spacepirates.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import spacepirates.game.Game;
import spacepirates.graphics.GameDisplay;
import spacepirates.input.Actions;
import spacepirates.input.PlayerInput;

public class GameDisplayScreen extends Screen{
	private float updateRate = 1/60f;
	
	private Matrix4 oldPro, oldView;
	private GameDisplay display;
	private Game game;
	private Container<Window> pauseMenu;
	private PlayerInput playerInput;
	private boolean paused;
	
	public GameDisplayScreen(Game game) {
		this.game = game;
		paused = false;
		
		oldPro = new Matrix4();
		oldView = new Matrix4();
		
		display = new GameDisplay(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		display.setTarget(game, game.getCamrea());
		
		
		playerInput = Actions.desktopDefault();
		playerInput.setMouseControl(display.getMouseControl());
		
		game.setPlayerInput(playerInput);
	}
	
	@Override
	public void render(SpriteBatch batch, float delta) {
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			setPaused(true);
		}
		
		if(!paused){
			game.update(updateRate);
			display.updateMouse(Gdx.input.getX(), Gdx.input.getY());
			
			oldPro.set(batch.getProjectionMatrix());
			oldView.set(batch.getTransformMatrix());
			
			display.render(batch, delta);
			
			batch.setProjectionMatrix(oldPro);
			batch.setTransformMatrix(oldView);
		}
		
		if(paused){
			batch.setColor(Color.GRAY);
		}
		
		batch.begin();
		display.draw(batch, delta);
		batch.end();
	}
	
	public void setPaused(boolean paused){
		this.paused = paused;
		pauseMenu.setVisible(paused);
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	@Override
	public void load(Skin skin) {
		game.setResources(getResources());
		
		Window win = new Window("Pause", skin);
		
		TextButton resumeButt = new TextButton("Resume", skin);
		resumeButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				setPaused(false);
			}
		});
		
		
		TextButton exitButt = new TextButton("Exit", skin);
		exitButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getManager().popScreen();
			}
		});
		
		win.add(resumeButt).pad(10, 10, 5, 10).row();
		win.add(exitButt).pad(5, 10, 10, 10);
		
		win.setMovable(false);
		
		pauseMenu = new Container<>(win);
		pauseMenu.setFillParent(true);
	}

	@Override
	public void show(Stage stage) {
		setPaused(false);
		stage.addActor(pauseMenu);
	}

	@Override
	public void hide(Stage stage) {
		stage.clear();
	}

	@Override
	public void dispose() {
		display.dispose();
	}

	@Override
	public void resize(int width, int height) {
		display.resize(width, height);
	}
}