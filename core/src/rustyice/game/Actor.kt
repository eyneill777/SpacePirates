package rustyice.game

import rustyice.editor.annotations.ComponentAccess
import rustyice.editor.annotations.ComponentProperty
import rustyice.game.Section
import rustyice.physics.Collidable
import rustyice.physics.Collision
import rustyice.physics.PhysicsComponent
import rustyice.physics.PointPhysicsComponent
import rustyice.resources.Resources

abstract class Actor : GameObject(), Collidable {
    override var section: Section? = null
        internal set

    var physicsComponent: PhysicsComponent = PointPhysicsComponent()
        @ComponentAccess get
        protected set

    override var x: Float
        @ComponentProperty("X") get() = physicsComponent.x
        @ComponentProperty("X") set(value) { physicsComponent.x = value }

    override var y: Float
        @ComponentProperty("Y") get() = physicsComponent.y
        @ComponentProperty("Y") set(value) { physicsComponent.y = value }

    override var rotation: Float
        @ComponentProperty("Rotation") get() = physicsComponent.rotation
        @ComponentProperty("Rotation") set(value) { physicsComponent.rotation = value }

    var width = 0f
    var height = 0f

    fun setSize(width: Float, height: Float) {
        this.width = width
        this.height = height
    }

    override fun init() {
        super.init()
        physicsComponent.parent = this
        physicsComponent.init()
    }

    override fun store() {
        super.store()
        physicsComponent.store()
    }

    override fun update(delta: Float) {
        physicsComponent.update(delta)
    }

    override fun beginCollision(collision: Collision) {
        physicsComponent.beginCollision(collision)
    }

    override fun endCollision(collision: Collision) {
        physicsComponent.endCollision(collision)
    }
}
