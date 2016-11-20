package rustyice.game.tiles

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import rustyice.editor.annotations.ComponentProperty
import rustyice.game.GameObject
import rustyice.game.Section
import rustyice.graphics.Camera
import rustyice.physics.*

abstract class Tile: GameObject, Collidable {
    @Transient
    var tileMap: TileMap? = null
        internal set

    override val section: Section?
        get() = tileMap?.section

    @Transient
    var sprite: Sprite? = null
        set(value) {
            field = value
            value?.setBounds(x, y, TILE_SIZE, TILE_SIZE)
        }

    var tilePhysics: TilePhysicsComponent? = null
        protected set

    var isSolid: Boolean private set
    var isOpaque: Boolean private set


    var tileX: Int = 0
        internal set(value){
            field = value
            tilePhysics?.x = value * TILE_SIZE
        }
    var tileY: Int = 0
        internal set(value){
            field = value
            tilePhysics?.y = value * TILE_SIZE
        }

    override var x: Float
        get() = tileX * TILE_SIZE
        set(value){}

    override var y: Float
        get() = tileY * TILE_SIZE
        set(value) {}

    @ComponentProperty
    override var rotation: Float = 0f
        set(value) {
            field = MathUtils.round(value / 90f) * 90f
            tilePhysics?.rotation = value
        }


    constructor(): super(){
        isSolid = false
        isOpaque = false
    }

    constructor(solid: Boolean = false, opaque: Boolean = false): super(){
        isSolid = solid
        isOpaque = opaque
    }

    override fun update(delta: Float) {
        tilePhysics?.update(delta)
    }

    override fun render(batch: Batch, camera: Camera) {
        sprite?.draw(batch)
    }

    override fun init() {
        super.init()

        tilePhysics?.parent = this
        tilePhysics?.init()
    }

    override fun store() {
        super.store()

        tilePhysics?.store()
    }

    override fun beginCollision(collision: Collision) {
        tilePhysics?.beginCollision(collision)
    }

    override fun endCollision(collision: Collision) {
        tilePhysics?.endCollision(collision)
    }
}
