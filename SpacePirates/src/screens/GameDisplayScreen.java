package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Game.GameWorld;
import graphics.GameDisplay;

public class GameDisplayScreen extends Screen{
	private Matrix4 oldPro, oldView;
	private GameDisplay display;
	private GameWorld world;
	private Container<Window> pauseMenu;
	private boolean paused;
	
	public GameDisplayScreen(GameWorld world) {
		this.world = world;
		paused = false;
		
		oldPro = new Matrix4();
		oldView = new Matrix4();
		
		display = new GameDisplay(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		display.setTarget(world, world.getCamrea());
	}
	
	@Override
	public void render(SpriteBatch batch, float delta) {
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			setPaused(true);
		}
		
		if(!paused){
			world.update(delta);
			world.getCamrea().setRotation(world.getCamrea().getRotation() - 10*delta);
			Vector2 mouse = display.screenToWorld(Gdx.input.getX(), Gdx.input.getY());
			
			world.getActor(0).setX(mouse.x);
			world.getActor(0).setY(mouse.y);
		
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
		display.draw(batch, delta, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
		world.setResources(getResources());
		
		Window win = new Window("Pause", skin);
		
		TextButton resumeButt = new TextButton("Resume", skin);
		resumeButt.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setPaused(false);
			}
		});
		
		
		TextButton exitButt = new TextButton("Exit", skin);
		exitButt.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				getManager().popScreen();
			}
		});
		
		win.add(resumeButt).pad(10, 10, 5, 10).row();
		win.add(exitButt).pad(5, 10, 10, 10);
		
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
