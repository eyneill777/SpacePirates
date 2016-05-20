package rustyice.screens;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import rustyice.resources.Resources;

/**
 * The temple for any screen, add it to the ScreenManager.
 * 
 * @author gabek
 * @see ScreenManager
 */
public abstract class Screen {

    private boolean loaded;
    private ScreenManager manager;
    private Stage stage;

    protected void setScreenManager(ScreenManager manager) {
        this.manager = manager;
        loaded = false;
        stage = manager.getStage();
    }

    public ScreenManager getManager() {
        return manager;
    }

    public TweenManager getTweenManager(){
        return manager.getTweenManager();
    }
    
    boolean isLoaded(){
        return loaded;
    }
    
    public Stage getStage() {
        return stage;
    }

    /**
     * Initializes all the screens widgets.
     * some widgets may require the assets are loaded so if that is the case construct them here.
     */
    public void load(){
        loaded = true;
    }

    /**
     * Called when the screen is signaled to appear.
     * @param stage
     */
    public abstract void show(Stage stage);

    /**
     * 
     * @param stage
     */
    public abstract void hide(Stage stage);

    public abstract void dispose();

    public abstract void resize(int width, int height);

    public abstract void render(SpriteBatch batch, float delta);
}
