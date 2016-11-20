package rustyice.game

import rustyice.editor.annotations.ComponentAccess
import rustyice.editor.annotations.ComponentProperty
import rustyice.game.Section
import rustyice.physics.Collidable
import rustyice.physics.Collision
import rustyice.physics.PhysicsComponent
import rustyice.physics.PointPhysicsComponent
import rustyengine.resources.Resources

abstract class Actor : GameObject(), Collidable {
    override var section: Section? = null
        internal set

    @ComponentAccess
    var physicsComponent: PhysicsComponent = PointPhysicsComponent()
        protected set

    @ComponentProperty
    override var x: Float
        get() = physicsComponent.x
        set(value) { physicsComponent.x = value }

    @ComponentProperty
    override var y: Float
        get() = physicsComponent.y
        set(value) { physicsComponent.y = value }

    @ComponentProperty
    override var rotation: Float
        get() = physicsComponent.rotation
        set(value) { physicsComponent.rotation = value }

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
