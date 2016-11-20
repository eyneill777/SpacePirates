package rustyice.game.lights

import box2dLight.Light
import box2dLight.RayHandler
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import rustyice.editor.annotations.ComponentAccess
import rustyice.editor.annotations.ComponentProperty
import rustyice.game.GameLifecycle
import rustyice.game.GameObject
import rustyice.game.MissingParentReference
import rustyice.graphics.Camera
import rustyice.physics.LIGHT
import rustyice.physics.OPAQUE

abstract class LightComponent: GameLifecycle() {
    val LIGHT_RES = 1f
    val DEFAULT_COLOR = Color(.75f, .75f, .75f, .75f)

    @Transient private var light: Light? = null
    @Transient var parent: GameObject? = null

    @ComponentProperty
    var color = DEFAULT_COLOR
        set(value) {
            field = value
            light?.color = value
        }

    @ComponentProperty
    var static = false
        set(value){
            field = value
            light?.isStaticLight = value
        }

    @ComponentProperty
    var xRay = false
        set(value){
            field = value
            light?.isXray = value
        }

    @ComponentProperty
    var distance = 10f
        set(value){
            field = value
            reInit()
        }
    var direction = 0f
        set(value) {
            field = value
            light?.direction = value
        }
    var x = 0f
        set(value){
            field = value
            light?.setPosition(value, y)
        }
    var y = 0f
        set(value){
            field = value
            light?.setPosition(x, value)
        }

    fun setPosition(x: Float, y: Float){
        this.x = x
        this.y = y
        light?.setPosition(x, y)
    }


    protected abstract fun buildLight(rayHandler: RayHandler): Light

    override fun init(){
        super.init()

        val rayHandler = parent?.game?.lightingHandler
            ?: throw MissingParentReference("Game")

        val light = buildLight(rayHandler)

        light.setContactFilter(LIGHT.toShort(), 0, OPAQUE.toShort())
        light.color = color
        light.distance = distance
        light.setPosition(x, y)
        light.isStaticLight = static
        light.isXray = xRay
        light.direction = direction

        this.light = light
    }

    override fun update(delta: Float) {}

    override fun render(batch: Batch, camera: Camera, renderFlags: Int) {}

    override fun store(){
        super.store()

        light?.remove()
        light = null
    }

    interface LightContainer{
        @ComponentAccess
        val lightComponent: LightComponent
    }
}
