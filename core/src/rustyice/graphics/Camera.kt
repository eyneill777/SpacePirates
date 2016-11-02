package rustyice.graphics;

import com.badlogic.gdx.math.MathUtils;

import rustyice.game.Actor;
import rustyice.game.GameObject

/**
 * @author gabek
 */
class Camera {
    var target: Actor?
    var isTracking: Boolean
    var relativeRotation: Boolean
    var x: Float = 0f
    var y: Float = 0f
    private var _rotation: Float = 0f
    var targetX: Float = 0f
    var targetY: Float = 0f
    var targetRot: Float = 0f
    var width: Float = 0f
    var height: Float = 0f
    var halfRenderSize: Float  = 0f
    var flags: Int  = 0

    var rotation: Float
        get() = _rotation
        set(value) {
            targetRot = value
        }

    constructor() {
        flags = NORMAL or LIGHTING

        target = null
        isTracking = false
        relativeRotation = false
    }

    constructor(width: Float, height: Float): this(){
        this.width = width
        this.height = height
    }

    fun checkFlag(flag: Int): Boolean{
        return (flags and flag) == flag
    }

    fun checkAnyFlag(flag: Int): Boolean{
        return (flags and flag) != 0
    }

    fun enableFlag(flag: Int){
        flags = flags or flag
    }

    fun disableFlag(flag: Int){
        flags = flags and flag.inv()
    }

    fun toggleFlag(flag: Int){
        flags = flags xor flag
    }

    fun setSize(width: Float, height: Float){
        this.width = width
        this.height = height
    }

    fun update(delta: Float) {
        val target = target

        if (isTracking && target != null) {
            if (relativeRotation) {
                _rotation = target.rotation + targetRot
            } else {
                _rotation = targetRot
            }

            x += MathUtils.clamp(target.x - x, -.5f, .5f)
            y += MathUtils.clamp(target.y - y, -.5f, .5f)
        } else {
            _rotation = targetRot
            x = targetX
            y = targetY
        }
    }

    fun apply(ortho: com.badlogic.gdx.graphics.Camera) {
        ortho.position.x = this.x
        ortho.position.y = this.y
        ortho.viewportWidth = width
        ortho.viewportHeight = height

        ortho.up.set(MathUtils.cosDeg(this._rotation + 90), MathUtils.sinDeg(this._rotation + 90), 0f)
        ortho.direction.set(0f, 0f, -1f)
    }
}
