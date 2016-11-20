package rustyice.game.tiles

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.esotericsoftware.minlog.Log
import rustyice.game.GameLifecycle
import rustyice.game.MissingParentReference

import rustyice.game.Section
import rustyice.graphics.Camera

class TileMap: GameLifecycle() {
    @Transient
    var body: Body? = null
        private set

    private var tileMap: Array<Tile?> = emptyArray()

    @Transient
    lateinit var section: Section

    private var mapW: Int = 0
    private var mapH: Int = 0
    private var offsetX: Int = 0
    private var offsetY: Int = 0

    fun setTile(tile: Tile, x: Int, y: Int) {
    	val minX: Int
        val minY: Int
        val wTemp: Int
        val hTemp: Int
        val w: Int
        val h: Int

        if(x < offsetX){
        	minX = x
        	wTemp = mapW + offsetX - x
        } else {
        	minX = offsetX
        	wTemp = mapW
        }
        
        if(y < offsetY){
        	minY = y
        	hTemp = mapH + offsetY - y
        } else {
        	minY = offsetY
        	hTemp = mapH
        }
        w = if(x - minX + 1 > wTemp) x - minX + 1 else wTemp
        h = if(y - minY + 1 > hTemp) y - minY + 1 else hTemp

        resize(minX, minY, w, h)

        val index = getIndex(x, y)

        tile.tileMap = this
        tile.tileX = x
        tile.tileY = y

        if (isInitialized) {
            tileMap[index]?.store()
            tile.init()
        }

        tileMap[index] = tile
        refreshArea(x, y)
    }

    private fun refreshArea(x: Int, y: Int){
        refreshTile(x-1, y)
        refreshTile(x+1, y)
        refreshTile(x, y-1)
        refreshTile(x, y+1)

        refreshTile(x-1, y-1)
        refreshTile(x+1, y+1)
        refreshTile(x+1, y-1)
        refreshTile(x-1, y+1)
    }

    private fun refreshTile(x: Int, y: Int){
        if(isInitialized) {
            val tile = getTile(x, y)
            tile?.reInit()
        }
    }
    
    fun resize(minX: Int, minY: Int, w: Int, h: Int) {
        if (minX < offsetX || minY < offsetY || w > mapW || h > mapH) {
            Log.debug(String.format("resize %d %d %d %d", minX, minY, w, h))
            val newMap = Array<Tile?>(w * h, { i -> null })

            for (y in 0 until mapH) {
                for (x in 0 until mapW) {
                    val oldIndex = y * mapW + x
                    val newIndex = (y + offsetY - minY) * w + (x + offsetX - minX)
                    newMap[newIndex] = tileMap[oldIndex]
                }
            }

            offsetX = minX
            offsetY = minY
            mapW = w
            mapH = h

            tileMap = newMap
        }
    }

    fun getTile(x: Int, y: Int): Tile? {
        if (x >= offsetX && y >= offsetY && x < mapW + offsetX && y < mapH + offsetY) {
            return tileMap[getIndex(x, y)]
        } else {
            return null
        }
    }

    fun getTileAt(x: Float, y: Float): Tile?{
        return getTile((x / TILE_SIZE - if(x<0) 1 else 0).toInt(),
                       (y / TILE_SIZE - if(y<0) 1 else 0).toInt())
    }

    override fun render(batch: Batch, camera: Camera) {
        val startX: Int = ((camera.x - camera.halfRenderSize) / TILE_SIZE).toInt() - 1
        val startY: Int = ((camera.y - camera.halfRenderSize) / TILE_SIZE).toInt() - 1
        val endX: Int = ((camera.x + camera.halfRenderSize) / TILE_SIZE).toInt() + 1
        val endY: Int = ((camera.y + camera.halfRenderSize) / TILE_SIZE).toInt() + 1

        for (y in startY .. endY) {
            for (x in startX .. endX) {
                getTile(x, y)?.render(batch, camera)
            }
        }

    }

    override fun update(delta: Float) {
        for (tile in tileMap) {
            tile?.update(delta)
        }
    }

    override fun init() {
        super.init()

        val world = section.game?.world ?: throw MissingParentReference("Game")

        val bodyDef = BodyDef()
        bodyDef.type = BodyType.StaticBody

        body = world.createBody(bodyDef)

        for (tile in tileMap) {
            if(tile != null){
                tile.tileMap = this
                tile.init()
            }
        }
    }

    override fun store() {
        super.store()

        for (tile in tileMap) {
            tile?.store()
        }

        val body = body
        if(body != null){
            section.game!!.world.destroyBody(body)
            this.body = null
        }
    }

    private fun getIndex(x: Int, y: Int): Int {
        return (y - offsetY) * mapW + (x - offsetX)
    }
}
