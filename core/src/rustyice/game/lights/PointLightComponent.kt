package rustyice.game.lights

import box2dLight.Light
import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.math.MathUtils

/**
 * @author gabek
 */
class PointLightComponent: LightComponent(){

    override fun buildLight(rayHandler: RayHandler): Light{
        return PointLight(rayHandler, getRayNum(distance, LIGHT_RES),
                color, distance, x, y)
    }

    private fun getRayNum(r: Float, space: Float): Int {
        return ((MathUtils.PI2 * r) / space).toInt()
    }
}
