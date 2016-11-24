package rustyice.game

import com.badlogic.gdx.graphics.g2d.Batch
import rustyice.graphics.Camera
import rustyice.graphics.RenderLayer

abstract class GameLifecycle{
    @Transient
    var isInitialized: Boolean = false
        private set

    open fun init(){
        if(isInitialized)
            throw IllegalStateException("Trying to initialized an object that's already initialized")
        isInitialized = true
    }
    open fun store(){
        if (!isInitialized)
            throw IllegalStateException("Trying to store an object that's already stored")
        isInitialized = false
    }

    fun reInit(){
        if(isInitialized){
            store()
            init()
        }
    }

    abstract fun update(delta: Float)
    abstract fun render(batch: Batch, camera: Camera, layer: RenderLayer)
}