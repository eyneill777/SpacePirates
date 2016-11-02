package rustyice.resources

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShaderProgram

object Resources: AssetManager(){

    lateinit var gameArt: TextureAtlas private set
    lateinit var box: TextureRegion private set
    lateinit var circle: TextureRegion private set

    lateinit var povShader: ShaderProgram private set

    fun init() {
        setLoader(ShaderProgram::class.java, ShaderLoader(fileHandleResolver))
    }
    
    fun startLoading(){
        load("art.atlas", TextureAtlas::class.java)
        load("shader/pov", ShaderProgram::class.java)
        load("gui/sfx/click3.wav", Sound::class.java)
    }
    
    fun loadAll() {
        finishLoading()
        gameArt = get("art.atlas", TextureAtlas::class.java)

        box = gameArt.findRegion("rect")
        circle = gameArt.findRegion("circle")

        povShader = get("shader/pov", ShaderProgram::class.java)
    }

    override fun dispose() {
        super.dispose()
    }
}