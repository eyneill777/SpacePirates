package gken.rustyice.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import gken.rustyice.resources.Resources;

import java.util.HashMap;
import java.util.Stack;

/**
 * Manages which screens are currently active and displays them.
 */
public class ScreenManager {
    private Stage stage;

    private HashMap<String, Screen> screens;
    private Stack<Screen> screenHistory;
    private Screen currentScreen;

    private Resources resources;
    
    /**
     * @param batch the Sprite batch all the rendering is done on
     * @param skin  the skin for the gui's, most of the time default will be used as of now
     */
    public ScreenManager(SpriteBatch batch, Resources resources) {
        stage = new Stage(new ScreenViewport(), batch);
        this.resources = resources;

        screens = new HashMap<>();
        screenHistory = new Stack<>();
    }

    /**
     * you need to add the stage to Gdx.input.setInputProcessor() or input will not work!!!!
     *
     * @return the stage the gui is run with
     */
    public Stage getStage() {
        return stage;
    }

    public Resources getResources(){
    	return resources;
    }
    
    /**
     * adds a screen with a given name
     *
     * @param name   the name the screen will be referenced to by
     * @param screen a screen object
     */
    public void addScreen(String name, Screen screen) {
        screens.put(name, screen);
        screen.setScreenManager(this);
        screen.load(resources.skin);
    }

    /**
     * displays a screen, the last screen will be added to a stack.
     *
     * @param name see add screen
     */
    public void showScreen(String name) {
    	if(currentScreen != null){
    		currentScreen.hide(stage);
    	}
        
    	Screen screen = screens.get(name);
        screen.show(stage);

        if (currentScreen != null) {
            screenHistory.push(currentScreen);
        }
        currentScreen = screen;
        currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * sets a screen to the screen before the last activation
     */
    public void popScreen() {
    	currentScreen.hide(stage);
        currentScreen = screenHistory.pop();
        currentScreen.show(stage);
    }

    /**
     * NEEDS to be called whenever the window resizes for screens to behave properly.
     *
     * @param width  in pixels
     * @param height in pixels
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        currentScreen.resize(width, height);
    }

    /**
     * NEEDS to be called every frame
     *
     * @param delta in seconds
     */
    public void render(SpriteBatch batch, float delta) {
        currentScreen.render(batch, delta);
        stage.act(delta);
        stage.draw();
    }
    
    public void dispose(){
    	for(Screen screen: screens.values()){
    		screen.dispose();
    	}
    	stage.dispose();
    }
}
