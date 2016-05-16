package rustyice.main;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.minlog.Log;
import com.kotcrab.vis.ui.VisUI;
import rustyice.editor.EditorScreen;
import rustyice.game.Game;
import rustyice.graphics.PerformanceTracker;
import rustyice.resources.Resources;
import rustyice.screens.GameDisplayScreen;
import rustyice.screens.LoadingScreen;
import rustyice.screens.ScreenManager;
import rustyice.screens.menus.MainMenu;
import rustyice.screens.menus.SettingsScreen;
import rustyice.screens.menus.effects.GuiAccessor;

public class GdxMain implements ApplicationListener {

    //private ShaderProgram badProgram;
    //private float time = 0;
    
    private Game game;
    private SpriteBatch batch;
    private ScreenManager screenManager;
    private Resources resources;

    private GeneralSettings settings;
    private FileHandle settingsFile;
    private PerformanceTracker tracker;
    private Kryo kryo;

    public GdxMain(GeneralSettings settings, FileHandle settingsFile) {
        this.settings = settings;
        this.settingsFile = settingsFile;
    }

    @Override
    public void create() {
        Log.set(Log.LEVEL_DEBUG);
        //GL30Profiler.enable();
        //GLProfiler.listener = new GLErrorLogger();
        
        VisUI.load(Gdx.files.internal("gui/uiskin.json"));
        Box2D.init();
        kryo = new Kryo();

        resources = new Resources();
        resources.startLoading();

        batch = new SpriteBatch();
        
        tracker = new PerformanceTracker(VisUI.getSkin().getFont("default-font"), false);

        screenManager = new ScreenManager(batch, resources, tracker);
        Gdx.input.setInputProcessor(screenManager.getStage());

        Tween.registerAccessor(Actor.class, new GuiAccessor());
        
        game = new Game();
        game.setResources(resources);
        
        screenManager.addScreen("loading", new LoadingScreen());
        screenManager.addScreen("main_menu", new MainMenu());
        screenManager.addScreen("settings", new SettingsScreen(settings, settingsFile));
        screenManager.addScreen("playing", new GameDisplayScreen(game));
        screenManager.addScreen("editor", new EditorScreen(game, kryo));

        screenManager.showScreen("loading");
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clear screen

        float delta = Gdx.graphics.getDeltaTime();
        
        //time += delta;
        //badProgram.setAttributef("time", time, 0, 0, 0);
        
        screenManager.render(batch, delta);

        tracker.update();
        GLProfiler.reset();
    }

    @Override
    public void resize(int width, int height) {
        screenManager.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        screenManager.dispose();
        game.dispose();
    }

    @Override
    public void resume() {
    } // don't need

    @Override
    public void pause() {
    } // don't need
}