package rustyice.screens;

import java.util.HashMap;
import java.util.Stack;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import rustyice.graphics.PerformanceTracker;
import rustyice.resources.Resources;

/**
 * Manages which screens are currently active and displays them.
 */
public class ScreenManager {

    private Stage stage;

    private HashMap<String, Screen> screens;
    private Stack<Screen> screenHistory;
    private Screen currentScreen;

    private Resources resources;
    private TweenManager tweenManager;
    
    private PerformanceTracker tracker;

    /**
     * @param batch the Sprite batch all the rendering is done on
     * @param skin the skin for the gui's, most of the time default will be used as of now
     */
    public ScreenManager(SpriteBatch batch, Resources resources, PerformanceTracker tracker) {
        this.tracker = tracker;

        this.stage = new Stage(new ScreenViewport(), batch);
        this.resources = resources;

        tweenManager = new TweenManager();
        
        this.screens = new HashMap<>();
        this.screenHistory = new Stack<>();
    }

    /**
     * you need to add the stage to Gdx.input.setInputProcessor() or input will not work!!!!
     *
     * @return the stage the gui is run with
     */
    public Stage getStage() {
        return this.stage;
    }

    public Resources getResources() {
        return this.resources;
    }
    
    public TweenManager getTweenManager(){
        return tweenManager;
    }

    /**
     * adds a screen with a given name
     *
     * @param name the name the screen will be referenced to by
     * @param screen a screen object
     */
    public void addScreen(String name, Screen screen) {
        this.screens.put(name, screen);
        screen.setScreenManager(this);
    }

    /**
     * displays a screen, the last screen will be added to a stack.
     *
     * @param name see add screen
     */
    public void showScreen(String name) {
        if (this.currentScreen != null) {
            this.currentScreen.hide(this.stage);
        }

        Screen screen = this.screens.get(name);
        
        if(!screen.isLoaded()){
            screen.load();
        }
        
        screen.show(this.stage);

        if (this.currentScreen != null) {
            this.screenHistory.push(this.currentScreen);
        }
        this.currentScreen = screen;
        
        this.currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        showTracker();
    }

    private void showTracker() {
        if (tracker != null) {
            stage.getActors().removeValue(tracker, true);
            stage.addActor(this.tracker);
            tracker.setFillParent(true);
            tracker.validate();
        }
    }

    /**
     * sets a screen to the screen before the last activation
     */
    public void popScreen() {
        currentScreen.hide(stage);
        currentScreen = screenHistory.pop();
        
        if(!currentScreen.isLoaded()){
            currentScreen.load();
        }
        
        currentScreen.show(stage);

        showTracker();
    }

    /**
     * NEEDS to be called whenever the window resizes for screens to behave properly.
     *
     * @param width in pixels
     * @param height in pixels
     */
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
        this.currentScreen.resize(width, height);

        if (this.tracker != null) {
            tracker.validate();
        }
    }

    /**
     * NEEDS to be called every frame
     *
     * @param delta in seconds
     */
    public void render(SpriteBatch batch, float delta) {
        tweenManager.update(delta);
        
        this.currentScreen.render(batch, delta);
        this.stage.act(delta);
        this.stage.draw();
    }

    public void dispose() {
        for (Screen screen : this.screens.values()) {
            screen.dispose();
        }
        this.stage.dispose();
    }
}
