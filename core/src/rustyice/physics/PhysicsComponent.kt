package rustyice.physics

import rustyice.game.*

/**
 * Created by gabek
 */
abstract class PhysicsComponent: GameLifecycle(), Collidable {
    @Transient
    open var parent: GameObject? = null

    val section: Section?
        get() = parent?.section
    val game: Game?
        get() = parent?.section?.game

    abstract var x: Float
    abstract var y: Float
    abstract var rotation: Float
}
