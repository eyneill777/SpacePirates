package rustyice.levelgenerators

import rustyice.game.Section

/**
 * @author Gabriel Keith
 */
interface LevelGenerator {
    fun generate(section: Section)
}