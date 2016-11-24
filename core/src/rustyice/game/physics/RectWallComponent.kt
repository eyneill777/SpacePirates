package rustyice.game.physics

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import rustyice.game.tiles.TILE_SIZE
import rustyice.game.tiles.Tile
import rustyice.game.tiles.TileMap
import rustyice.graphics.Camera
import rustyice.graphics.RenderLayer
import rustyice.physics.*

/**
 * @author gabek
 */
class RectWallComponent: TilePhysicsComponent() {
    private val FIXTURES_SIZE = 4
    private val N = 0
    private val S = 1
    private val E = 2
    private val W = 3

    @Transient private val fixtures = Array<Fixture?>(FIXTURES_SIZE, {null})

    override var x = 0f
    override var y = 0f
    override var rotation = 0f

    private fun checkTileSolid(parent: Tile, tileMap: TileMap, rx: Int, ry: Int): Boolean{
        val other = tileMap.getTile(parent.tileX + rx, parent.tileY + ry)
        return other != null && other.isSolid && (!parent.isOpaque || other.isOpaque)
    }

    private fun initSolid(parent: Tile, tileMap: TileMap){
        val body = tileMap.body

        if(body != null) {
            val n = checkTileSolid(parent, tileMap, 0, 1)
            val s = checkTileSolid(parent, tileMap, 0,-1)
            val e = checkTileSolid(parent, tileMap, 1, 0)
            val w = checkTileSolid(parent, tileMap,-1, 0)

            if (!n || !s || !e || !w) {
                val fixtureDef = FixtureDef()
                val edge = EdgeShape()
                fixtureDef.shape = edge
                fixtureDef.filter.categoryBits = WALL.toShort()
                fixtureDef.filter.maskBits = (LARGE or SMALL).toShort()

                val x1 = TILE_SIZE * parent.tileX
                val y1 = TILE_SIZE * parent.tileY
                val x2 = x1 + TILE_SIZE
                val y2 = y1 + TILE_SIZE

                if (!n) {
                    edge.set(x1, y2, x2, y2)
                    if (w) {
                        edge.setHasVertex0(true)
                        edge.setVertex0(x1 - TILE_SIZE, y2)
                    }
                    if (e) {
                        edge.setHasVertex3(true)
                        edge.setVertex3(x2 + TILE_SIZE, y2)
                    }
                    fixtures[N] = buildFix(body, fixtureDef, parent)
                }

                if (!s) {
                    edge.set(x2, y1, x1, y1)
                    if (e) {
                        edge.setHasVertex0(true)
                        edge.setVertex0(x2 + TILE_SIZE, y1)
                    }
                    if (w) {
                        edge.setHasVertex3(true)
                        edge.setVertex3(x1 - TILE_SIZE, y1)
                    }
                    fixtures[S] = buildFix(body, fixtureDef, parent)
                }

                if (!e) {
                    edge.set(x2, y2, x2, y1)
                    if (n) {
                        edge.setHasVertex0(true)
                        edge.setVertex0(x2, y2 + TILE_SIZE)
                    }
                    if (s) {
                        edge.setHasVertex3(true)
                        edge.setVertex3(x2, y1 - TILE_SIZE)
                    }
                    fixtures[E] = buildFix(body, fixtureDef, parent)
                }

                if (!w) {
                    edge.set(x1, y1, x1, y2)
                    if (s) {
                        edge.setHasVertex0(true)
                        edge.setVertex0(x1, y1 - TILE_SIZE)
                    }
                    if (n) {
                        edge.setHasVertex3(true)
                        edge.setVertex3(x1, y2 + TILE_SIZE)
                    }
                    fixtures[W] = buildFix(body, fixtureDef, parent)
                }

                edge.dispose()
            }
        }
    }

    private fun buildFix(body: Body, def: FixtureDef, master: Tile): Fixture{
        val newFix = body.createFixture(def)
        newFix.userData = master
        return newFix
    }

    override fun init() {
        super.init()

        val parentTile = parentTile!!
        val tileMap = parentTile.tileMap!!


        if(parentTile.isSolid){
            initSolid(parentTile, tileMap)
        }
    }

    override fun store() {
        super.store()

        val parent = parentTile!!
        val tileMap = parent.tileMap!!
        val body = tileMap.body!!

        for(i in fixtures.indices) {
            val fixture = fixtures[i]
            if (fixture != null) {
                body.destroyFixture(fixture)
                fixtures[i] = null
            }
        }
    }

    override fun update(delta: Float) { }

    override fun render(batch: Batch, camera: Camera, layer: RenderLayer) {}

    override fun beginCollision(collision: Collision) { }

    override fun endCollision(collision: Collision) { }
}
