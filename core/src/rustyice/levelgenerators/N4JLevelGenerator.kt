package rustyice.levelgenerators

import com.badlogic.gdx.graphics.Color
import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import rustyice.game.Section
import rustyice.game.actors.PointLight
import rustyice.game.tiles.FloorTile
import rustyice.game.tiles.TILE_SIZE
import rustyice.game.tiles.WallTile
import java.util.*

/**
 * @author Gabriel Keith
 */
class N4JLevelGenerator : LevelGenerator {
    override fun generate(section: Section) {
        val grid = Grid(50)

        val dungeonGenerator = DungeonGenerator()
        dungeonGenerator.roomGenerationAttempts = 500
        dungeonGenerator.maxRoomSize = 5
        dungeonGenerator.tolerance = 10
        dungeonGenerator.minRoomSize = 3
        dungeonGenerator.generate(grid)

        section.tiles.resize(0, 0, grid.width, grid.height)

        val rand = Random()
        for (x in 0 until grid.width * 2) {
            for (y in 0 until grid.height * 2) {
                if(grid.get(x/2, y/2) == 1f){
                    section.tiles.setTile(WallTile(), x, y)
                } else {
                    section.tiles.setTile(FloorTile(), x, y)
                    if(rand.nextDouble() < 0.005){
                        val light = PointLight()
                        light.lightComponent.color = Color(0f, 0f, 0f, 1f)
                        light.lightComponent.distance = 15f
                        light.x = x * TILE_SIZE
                        light.y = y * TILE_SIZE
                        section.addActor(light)
                    }
                }
            }
        }

        dungeonGenerator.roomTypes
    }
}