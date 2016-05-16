package rustyice.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import rustyice.game.Game;
import rustyice.graphics.GameDisplay;
import rustyice.input.Actions;
import rustyice.input.PlayerInput;
import rustyice.screens.menus.effects.GuiAccessor;
import rustyice.screens.menus.effects.GuiEffects;

public class GameDisplayScreen extends Screen {
    private GameDisplay display;
    private GameDisplay display2, display3, display4;
    private Game game;
    private VisWindow pauseWindow;
    private Container<VisWindow> pauseMenu;
    private VisTable root;
    private PlayerInput playerInput;
    private boolean paused;
    // private Box2DDebugRenderer debugRender;

    public GameDisplayScreen(Game game) {
        this.game = game;
        paused = false;

        display = new GameDisplay();
        display.setTarget(game, game.getCamera(0));

        playerInput = Actions.desktopDefault();

        game.setPlayerInput(playerInput);
        
        display2 = new GameDisplay();
        display2.setTarget(game, game.getCamera(0));
        
        display3 = new GameDisplay();
        display3.setTarget(game, game.getCamera(0));
        
        display4 = new GameDisplay();
        display4.setTarget(game, game.getCamera(0));
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            setPaused(true);
        }

        if (!paused) {
            game.update(delta);
            display.render(batch, delta);
            display2.render(batch, delta);
            display3.render(batch, delta);
            display4.render(batch, delta);
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        
        if(paused){
            pauseMenu.setVisible(true);
            pauseWindow.getColor().a = 0;
            Tween.to(pauseWindow, GuiAccessor.ALPHA, 0.5f).target(1).start(getTweenManager());
        } else {
            Tween.to(pauseWindow, GuiAccessor.ALPHA, 0.5f).target(0).setCallback(
                    (int flag, BaseTween<?> arg1) ->{pauseMenu.setVisible(false);}
                    ).start(getTweenManager());
            }
        }

    public boolean isPaused() {
        return paused;
    }

    @Override
    public void load() {
        super.load();

        pauseWindow = new VisWindow("Pause");

        VisTextButton resumeButt = new VisTextButton("Resume");
        resumeButt.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused(false);
            }
        });

        VisTextButton settingsButt = new VisTextButton("Settings");
        settingsButt.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                getManager().showScreen("settings");
            }
        });

        VisTextButton exitButt = new VisTextButton("Exit");
        exitButt.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                getManager().popScreen();
            }
        });

        pauseWindow.add(resumeButt).pad(5).prefWidth(180).row();
        pauseWindow.add(settingsButt).pad(5).prefWidth(180).row();
        pauseWindow.add(exitButt).pad(5).prefWidth(180);

        pauseWindow.setMovable(false);

        pauseMenu = new Container<>(pauseWindow);
        pauseMenu.setFillParent(true);
        pauseMenu.setVisible(false);
        
        root = new VisTable();
        root.add(display).grow();
        root.add(display2).grow();
        root.row();
        root.add(display3).grow();
        root.add(display4).grow();
        
        root.setFillParent(true);
    }

    @Override
    public void show(Stage stage) {
        // VisWindow window = new VisWindow("Test");
        // window.setFillParent(true);
        // window.add(this.display).grow();
        // window.setResizable(true);
        stage.addActor(root);
        stage.addActor(pauseMenu);
        
        root.pack();
        
        GuiEffects.fadeIn(root, 0.5f).start(getTweenManager());
        GuiEffects.fadeIn(pauseMenu, 0.5f).start(getTweenManager());
    }

    @Override
    public void hide(Stage stage) {
        
        GuiEffects.fadeOut(root, 0.5f, () ->{
            getStage().getActors().removeValue(root, true);
        }).start(getTweenManager());
        
        GuiEffects.fadeOut(pauseMenu, 0.5f, () ->{
            getStage().getActors().removeValue(pauseMenu, true);
        }).start(getTweenManager());
    }

    @Override
    public void dispose() {
        display.dispose();
        display2.dispose();
        // debugRender.dispose();
    }

    @Override
    public void resize(int width, int height) {
        // this.display.setSize(width, height);
    }
}