package rustyice.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import rustyice.game.Game;
import rustyice.graphics.GameDisplay;
import rustyice.input.Actions;
import rustyice.input.PlayerInput;

public class GameDisplayScreen extends Screen{
	private float updateRate = 1/60f;

    private Matrix4 oldPro, oldView;
	private GameDisplay display;
	private Game game;
	private Container<VisWindow> pauseMenu;
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
		display.setTarget(game, game.getCamera(0));
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
            
            display.render(batch, game.getRayHandler(), delta);

            batch.setProjectionMatrix(oldPro);
            batch.setTransformMatrix(oldView);
		}
		
		//if(paused){
		//	batch.setColor(Color.GRAY);
		//}

        //game.getRayHandler().setShadows(false);
		
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
	public void load() {
		game.setResources(getResources());
		
		VisWindow win = new VisWindow("Pause");
		
		VisTextButton resumeButt = new VisTextButton("Resume");
		resumeButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				setPaused(false);
			}
		});
		
		VisTextButton settingsButt = new VisTextButton("Settings");
		settingsButt.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				getManager().showScreen("settings");
			}
		});
		
		VisTextButton exitButt = new VisTextButton("Exit");
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
		
		fpsLable = new VisLabel();
		fpsLable.setColor(Color.WHITE);
	}

	@Override
	public void show(Stage stage) {
		stage.addActor(display);
		stage.addActor(pauseMenu);
		stage.addActor(fpsLable);
		//fpsLable.setPosition(Gdx.graphics.getWidth()-75,
		//		Gdx.graphics.getHeight()-50);
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
		fpsLable.setPosition(width-fpsLable.getWidth() - 100,
				height-fpsLable.getHeight() - 50);
	}
}