package gken.rustyice.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gken.rustyice.game.Game;
import gken.rustyice.input.Actions;
import gken.rustyice.input.PlayerInput;
import gken.rustyice.graphics.GameDisplay;
import gken.rustyice.graphics.GraphicsUtils;

public class GameDisplayScreen extends Screen{
	private float updateRate = 1/60f;

    private Matrix4 oldPro, oldView;
	private GameDisplay display;
	private Game game;
	private Container<Window> pauseMenu;
	private PlayerInput playerInput;
	private boolean paused;
	
	private Label fpsLable;
	//private Box2DDebugRenderer debugRender;
	
	public GameDisplayScreen(Game game) {
		this.game = game;
		paused = false;

        oldPro = new Matrix4();
        oldView = new Matrix4();

		display = new GameDisplay(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		display.setTarget(game, game.getCamera());
		display.setFillParent(true);

		playerInput = Actions.desktopDefault();
		playerInput.setMouseControl(display.getMouseControl());
		
		game.setPlayerInput(playerInput);
		//debugRender = new Box2DDebugRenderer(true, true, true, true, true, true);
	}
	
	@Override
	public void render(SpriteBatch batch, float delta) {
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			setPaused(true);
		}
		
		
		if(!paused){
			game.update(updateRate);
			display.updateProjection();
			display.updateMouse(Gdx.input.getX(), Gdx.input.getY());
			
            oldPro.set(batch.getProjectionMatrix());
            oldView.set(batch.getTransformMatrix());

            display.render(batch, delta);

            batch.setProjectionMatrix(oldPro);
            batch.setTransformMatrix(oldView);
		}
		
		//if(paused){
		//	batch.setColor(Color.GRAY);
		//}
        batch.begin();
        display.draw(batch, 1f);
        batch.end();

        game.getRayHandler().setCombinedMatrix(display.getOrtho());
        game.getRayHandler().updateAndRender();
		
        fpsLable.setText(Integer.toString(Gdx.graphics.getFramesPerSecond()));
		//debugRender.render(game.getWorld(), display.getOrtho().combined);
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
		
		TextButton settingsButt = new TextButton("Settings", skin);
		settingsButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				getManager().showScreen("settings");
			}
		});
		
		TextButton exitButt = new TextButton("Exit", skin);
		exitButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getManager().popScreen();
			}
		});
		
		win.add(resumeButt).pad(5).prefWidth(180).row();
		win.add(settingsButt).pad(5).prefWidth(180).row();
		win.add(exitButt).pad(5).prefWidth(180);
		
		win.setMovable(false);
		
		pauseMenu = new Container<>(win);
		pauseMenu.setFillParent(true);
		pauseMenu.setVisible(false);
		
		fpsLable = new Label("", skin, "fps");
		fpsLable.setColor(Color.WHITE);
	}

	@Override
	public void show(Stage stage) {
		//stage.addActor(display);
		stage.addActor(pauseMenu);
		stage.addActor(fpsLable);
		fpsLable.setPosition(Gdx.graphics.getWidth()-50,
				Gdx.graphics.getHeight()-50);
	}

	@Override
	public void hide(Stage stage) {
		stage.clear();
	}

	@Override
	public void dispose() {
		display.dispose();
		//debugRender.dispose();
	}

	@Override
	public void resize(int width, int height) {
		display.setSize(width, height);
		game.getRayHandler().resizeFBO(width/2, height/2);
		fpsLable.setPosition(width-fpsLable.getWidth() - 50,
				height-fpsLable.getHeight() - 50);
	}
}