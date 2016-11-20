package rustyice.physics

import com.badlogic.gdx.graphics.g2d.Batch
import rustyice.graphics.Camera

class PointPhysicsComponent: PhysicsComponent() {
    override var x: Float = 0f
    override var y: Float = 0f
    override var rotation: Float = 0f

    override fun update(delta: Float) {}
    override fun render(batch: Batch, camera: Camera) {}
    override fun beginCollision(collision: Collision) {}
    override fun endCollision(collision: Collision) {}
}
