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
import rustyice.game.characters.Player;
import rustyice.graphics.Camera;
import rustyice.graphics.GameDisplay;
import rustyice.graphics.RenderFlags;
import rustyice.input.Actions;
import rustyice.input.PlayerInput;
import rustyice.screens.effects.GuiAccessor;
import rustyice.screens.effects.GuiEffects;

public class GameDisplayScreen extends Screen {
    private GameDisplay display;
    private Camera camera;
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
        camera = new Camera(12, 12);
        camera.enableFlag(RenderFlags.POV);

        display.setTarget(game, camera);

        playerInput = Actions.desktopDefault();
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            setPaused(true);
        }

        if (!paused) {
            game.update(delta);
            camera.update(delta);
            display.render(batch, delta);
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
        
        root.setFillParent(true);
    }

    @Override
    public void show(Stage stage) {
        stage.addActor(root);
        stage.addActor(pauseMenu);
        
        root.pack();
        
        GuiEffects.fadeIn(root, 0.5f).start(getTweenManager());
        GuiEffects.fadeIn(pauseMenu, 0.5f).start(getTweenManager());

        game.finishLoadingSection();

        game.getCurrentSection().getActors().stream().filter(actor -> actor instanceof Player).forEach(actor -> {
            ((Player) actor).setPlayerInput(playerInput);
            camera.setTarget(actor);
            camera.setTracking(true);
        });
    }

    @Override
    public void hide(Stage stage) {
        
        GuiEffects.fadeOut(root, 0.5f, () ->
            getStage().getActors().removeValue(root, true)
        ).start(getTweenManager());
        
        GuiEffects.fadeOut(pauseMenu, 0.5f, () ->
                getStage().getActors().removeValue(pauseMenu, true)
        ).start(getTweenManager());
    }

    @Override
    public void dispose() {
        display.dispose();
    }

    @Override
    public void resize(int width, int height) {
        // this.display.setSize(width, height);
    }
}