package rustyengine.resources

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import kotlin.reflect.KClass

class Resources{
    private val assetManager: AssetManager = AssetManager()

    lateinit var box: Texture private set
    lateinit var circle: Texture private set

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

    fun <Type: Any> get(fileName: String, type: KClass<Type>): Type{
        return assetManager.get(fileName, type.java)
    }

    fun findRegion(fileName: String, regionName: String): TextureRegion{
        return get(fileName, TextureAtlas::class).findRegion(regionName)
    }

    fun update(): Boolean{
        return assetManager.update()
    }
    
    fun loadAll() {
        assetManager.finishLoading()


        buildShapes()
    }

    private fun buildShapes(){
        val size = 64

        val pixmap = Pixmap(size, size, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.CLEAR)
        pixmap.fill()

        pixmap.setColor(Color.WHITE)
        pixmap.fillCircle(size/2, size/2, size/2)
        circle = Texture(pixmap)

        pixmap.fill()
        box = Texture(pixmap)

        pixmap.dispose()
    }

    fun dispose() {
        assetManager.dispose()
        box.dispose()
        circle.dispose()
    }
}