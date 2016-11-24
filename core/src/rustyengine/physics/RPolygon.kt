package rustyengine.physics

import com.badlogic.gdx.physics.box2d.PolygonShape


class RPolygon : RShape() {
    var vertices: FloatArray = FloatArray(0)

    override fun preInit() {
        val polyShape = PolygonShape()
        polyShape.set(vertices)
        shape = polyShape
    }
}