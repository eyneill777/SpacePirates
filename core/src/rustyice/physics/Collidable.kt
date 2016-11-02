package rustyice.physics;

interface Collidable {
    fun beginCollision(collision: Collision)
    fun endCollision(collision: Collision)
}