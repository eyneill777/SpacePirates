package rustyice.game

import com.badlogic.gdx.graphics.g2d.Batch
import rustyice.game.tiles.TileMap
import rustyice.graphics.Camera
import rustyice.graphics.RenderLayer
import java.util.*

class Section: GameLifecycle() {

    val actors: ArrayList<Actor>
    val tiles: TileMap

    @Transient
    var game: Game? = null
    @Transient
    private val addList: ArrayList<Actor>
    @Transient
    private val removeList: ArrayList<Actor>

    init {
        addList = ArrayList()
        removeList = ArrayList()

        actors = ArrayList()
        tiles = TileMap()
    }

    override fun init() {
        super.init()

        tiles.section = this
        tiles.init()

        for (actor in actors) {
            actor.section = this
            actor.init()
        }
    }

    override fun store() {
        super.store()

        finishAddingActors()

        actors.forEach(Actor::store)
    }

    fun finishAddingActors() {
        for (actor in addList) {
            actors.add(actor)
            actor.init()
        }
        addList.clear()
    }

    override fun update(delta: Float) {
        finishAddingActors()

        for (actor in actors) {
            actor.update(delta)
        }

        tiles.update(delta)

        for (actor in removeList) {
            actors.remove(actor)
            actor.store()
        }
        removeList.clear()
    }

    override fun render(batch: Batch, camera: Camera, layer: RenderLayer) {
        tiles.render(batch, camera, layer)

        for(actor in actors) {
            if(actor.x + actor.width / 2 > camera.x - camera.halfRenderSize
                    && actor.x - actor.width / 2 < camera.x + camera.halfRenderSize
                    && actor.y + actor.height / 2 > camera.y - camera.halfRenderSize
                    && actor.y - actor.height / 2 < camera.y + camera.halfRenderSize) {
                actor.render(batch, camera, layer)
            }
        }
    }

    fun addActor(actor: Actor) {
        if (isInitialized) {
            addList.add(actor)
        } else {
            actors.add(actor)
        }
        actor.section = this
    }

    fun removeActor(actor: Actor) {
        if(isInitialized){
            removeList.add(actor)
        } else {
            actors.remove(actor)
        }
    }

    fun getActor(index: Int): Actor {
        return actors[index]
    }
}
