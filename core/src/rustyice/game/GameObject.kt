package rustyice.game

abstract class GameObject: GameLifecycle(){

    abstract val section: Section?
    val game: Game? get() = section?.game

    abstract var x: Float
    abstract var y: Float
    abstract var rotation: Float

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }
}
