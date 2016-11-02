package rustyice.game.lights

import box2dLight.ConeLight
import box2dLight.Light
import box2dLight.RayHandler
import com.badlogic.gdx.math.MathUtils
import rustyice.editor.annotations.ComponentProperty

/**
 * @author gabek
 */
class ConeLightComponent: LightComponent{
    var coneDegree: Float
        @ComponentProperty("Cone Degree") get
        @ComponentProperty("Cone Degree")
        set(value) {
            field = value
            if(isInitialized){
                reInit()
            }
        }

    constructor(): super(){
        coneDegree = 25f
    }

    override fun buildLight(rayHandler: RayHandler): Light {
        val coneLight = ConeLight(rayHandler, getRayNum(distance, LIGHT_RES, coneDegree), color,
                distance, x, y, direction, coneDegree)

        return coneLight
    }

    private fun getRayNum(r: Float, space: Float, coneDegree: Float): Int {
        return ((coneDegree * MathUtils.PI2 * r) / (space * 180)).toInt()
    }
}
