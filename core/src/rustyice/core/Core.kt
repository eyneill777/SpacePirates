package rustyice.core

import aurelienribon.tweenengine.Tween
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.scenes.scene2d.Actor
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer
import com.esotericsoftware.minlog.Log
import com.kotcrab.vis.ui.VisUI
import rustyice.editor.EditorScreen
import rustyice.game.Game
import rustyice.graphics.GLErrorLogger
import rustyice.graphics.PerformanceTracker
import rustyice.resources.Resources
import rustyice.screens.GameDisplayScreen
import rustyice.screens.LoadingScreen
import rustyice.screens.ScreenManager
import rustyice.screens.effects.GuiAccessor
import rustyice.screens.menus.MainMenu
import rustyice.screens.menus.SettingsScreen

object Core: ApplicationListener {
    private lateinit var game: Game
    private lateinit var batch: SpriteBatch
    private lateinit var screenManager: ScreenManager
    //private PerformanceTracker tracker;
    lateinit var kryo: Kryo private set

    private lateinit var tracker: PerformanceTracker

    override fun create() {
        Log.set(Log.LEVEL_DEBUG)
        GLProfiler.enable()

        Gdx.gl.glDepthMask(false)
        GLProfiler.listener = GLErrorLogger

        VisUI.load(Gdx.files.internal("gui/uiskin.json"))
        //VisUI.load()
        Box2D.init()

        kryo = Kryo()
        kryo.setDefaultSerializer(CompatibleFieldSerializer::class.java)

        Resources.init()
        Resources.startLoading()

        batch = SpriteBatch()

        tracker = PerformanceTracker(VisUI.getSkin().getFont("default-font"), true)

        screenManager = ScreenManager(batch, tracker)
        Gdx.input.inputProcessor = screenManager.stage

        Tween.registerAccessor(Actor::class.java, GuiAccessor())
        game = Game()
        
        screenManager.addScreen("loading", {LoadingScreen()})
        screenManager.addScreen("main_menu", {MainMenu()})
        screenManager.addScreen("settings", {SettingsScreen()})
        screenManager.addScreen("playing", {GameDisplayScreen(game)})
        screenManager.addScreen("editor", {EditorScreen(game, kryo)})

        screenManager.showScreen("loading")
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) // clear screen

        val delta = Gdx.graphics.deltaTime
        
        //time += delta;
        //badProgram.setAttributef("time", time, 0, 0, 0);
        
        screenManager.render(batch, delta)

        tracker.update()
        GLProfiler.reset()
    }

    override fun resize(width: Int, height: Int) {
        screenManager.resize(width, height)
    }

    override fun dispose() {
        batch.dispose()
        screenManager.dispose()
        game.dispose()

        GeneralSettings.save()
    }

    override fun resume() {
    } // don't need

    override fun pause() {
    } // don't need
}