package rustyengine.physics

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.CircleShape

/**
 * @author Gabriel Keith
 */
class RCircle : RShape() {
    var x = 0f
    var y = 0f
    var radius: Float = 0f

    override fun preInit() {
        val circleShape = CircleShape()
        circleShape.radius = radius
        circleShape.position = Vector2(x, y)
        shape = circleShape
    }
}