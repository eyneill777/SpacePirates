package screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import resources.Resources;

/**
 * The temples for any screen, add it to the ScreenManager, come on try it.
 * @author gabek
 * @see ScreenManager
 */
public abstract class Screen{
	private ScreenManager manager;
	private Stage stage;
	private Resources resources;

	protected void setScreenManager(ScreenManager manager){
		this.manager = manager;
		stage = manager.getStage();
		resources = manager.getResources();
	}
	
	public ScreenManager getManager(){
		return manager;
	}
	
	public Stage getStage(){
		return stage;
	}
	
	public Resources getResources(){
		return resources;
	}
	
	public abstract void load(Skin skin);

	public abstract void show(Stage stage);
	
	public abstract void hide(Stage stage);
	
	public abstract void dispose();
	
	public abstract void resize(int width, int height);
	
	public abstract void render(SpriteBatch batch, float delta);
}
