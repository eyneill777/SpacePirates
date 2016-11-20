package rustyengine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.kotcrab.vis.ui.widget.file.FileChooser
import rustyengine.resources.Resources
import rustyice.game.Game
import rustyice.graphics.PerformanceTracker
import rustyice.screens.ScreenManager

object RustyEngine: KodeinAware{
    override val kodein: ConfigurableKodein = ConfigurableKodein()

    lateinit var appid: String

    lateinit var resorces: Resources private set
    lateinit var batch: SpriteBatch private set
    lateinit var viewport: Viewport private set
    lateinit var screenManager: ScreenManager private set
    var tracker: PerformanceTracker? = null
        private set


    fun init(appid: String, tracker: PerformanceTracker? = null){
        this.appid = appid

        this.tracker = tracker

        resorces = Resources()
        resorces.init()
        resorces.startLoading()
        batch = SpriteBatch()

        val viewport = ScreenViewport()
        viewport.unitsPerPixel = 0.5f
        this.viewport = viewport

        FileChooser.setDefaultPrefsName("$appid.filechooser")
        screenManager = ScreenManager(tracker)
        Gdx.input.inputProcessor = screenManager.stage
    }

    fun resize(w: Int, h: Int){
        screenManager.resize(w, h)
    }

    fun render(){
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) // clear screen

        val delta = Gdx.graphics.deltaTime
        screenManager.render(batch, delta)

        tracker?.update()
    }

    fun dispose(){
        batch.dispose()
        screenManager.dispose()
    }

    fun initScreens(onInit: ScreenManager.() -> Unit ){
        onInit(screenManager)
    }
}