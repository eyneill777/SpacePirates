package rustyengine.resources

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import kotlin.reflect.KClass

class Resources{
    private val assetManager: AssetManager = AssetManager()

    lateinit var gameArt: TextureAtlas private set
    lateinit var box: TextureRegion private set
    lateinit var circle: TextureRegion private set

    val progress: Float get() = assetManager.progress

    fun init() {
        assetManager.setLoader(ShaderProgram::class.java, ShaderLoader(assetManager.fileHandleResolver))
    }

    fun load(fileName: String, type: KClass<*>){
        assetManager.load(fileName, type.java)
    }

    fun <Type> get(fileName: String): Type{
        return assetManager.get(fileName)
    }

    fun update(): Boolean{
        return assetManager.update()
    }

    fun startLoading(){
        load("art.atlas", TextureAtlas::class)
        load("gui/sfx/click3.wav", Sound::class)
    }
    
    fun loadAll() {
        assetManager.finishLoading()
        gameArt = get("art.atlas")

        box = gameArt.findRegion("rect")
        circle = gameArt.findRegion("circle")
    }

    fun dispose() {
        assetManager.dispose()
    }
}