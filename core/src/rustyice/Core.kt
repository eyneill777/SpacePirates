package rustyice

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.physics.box2d.Box2D
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer
import com.esotericsoftware.minlog.Log
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.kotcrab.vis.ui.VisUI
import rustyengine.RustyEngine
import rustyengine.resources.Resources
import rustyengine.GeneralSettings
import rustyice.editor.EditorScreen
import rustyice.game.Game
import rustyice.graphics.PerformanceTracker
import rustyice.screens.GameDisplayScreen
import rustyice.screens.LoadingScreen
import rustyice.screens.menus.MainMenu
import rustyice.screens.menus.SettingsScreen

object Core: ApplicationListener {
    lateinit var tracker: PerformanceTracker


    override fun create() {
        Log.set(Log.LEVEL_DEBUG)
        //GLProfiler.enable()
        //GLProfiler.listener = GLErrorLogger

        VisUI.load(Gdx.files.internal("gui/uiskin.json"))
        //VisUI.load()
        tracker = PerformanceTracker(VisUI.getSkin().getFont("default-font"), false)

        Box2D.init()

        RustyEngine.init("rustyice", tracker)
        RustyEngine.initScreens {
            addScreen("loading") {
                LoadingScreen()
            }
            addScreen("main_menu") {
                MainMenu()
            }
            addScreen("settings") {
                SettingsScreen()
            }
            addScreen("playing") {
                GameDisplayScreen()
            }
            addScreen("editor") {
                EditorScreen()
            }

            showScreen("loading")
        }
    }

    override fun render() {
        RustyEngine.render()

        //GLProfiler.reset()
    }

    override fun resize(width: Int, height: Int) {
        RustyEngine.resize(width, height)
    }

    override fun dispose() {
        RustyEngine.dispose()

        GeneralSettings.save()
    }

    override fun resume() {
    } // don't need

    override fun pause() {
    } // don't need
}