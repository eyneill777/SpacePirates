package rustyice.editor

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.VisScrollPane
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import rustyice.game.actors.ConeLight
import rustyice.game.actors.Player
import rustyice.game.actors.PointLight
import rustyice.game.actors.TestActor
import rustyice.game.tiles.*
import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor

/**
 * @author gabek
 */
class EditorSelectionPane {
    val table: VisTable
    private val actorTree: SelectionTree
    private val tileTree: SelectionTree
    private val selectionScroll: VisScrollPane

    private val modeGroup: ButtonGroup<TextButton>
    private val actorModeButt: VisTextButton
    private val tileModeButt: VisTextButton

    private var _mode: Mode = Mode.ACTORS
    var mode: Mode
        get() = _mode
        set(value) {
            _mode = value
            when (value) {
                Mode.ACTORS -> tileModeButt.isChecked = true
                Mode.TILES -> actorModeButt.isChecked = true
            }
        }

    init {
        actorModeButt = VisTextButton("Actors", "toggle")
        tileModeButt = VisTextButton("Tiles", "toggle")

        val modeListener = ModeChangeListener()
        actorModeButt.addListener(modeListener)
        tileModeButt.addListener(modeListener)

        modeGroup = ButtonGroup(actorModeButt, tileModeButt)
        modeGroup.setMaxCheckCount(1)
        modeGroup.setMinCheckCount(1)

        actorTree = SelectionTree()
        tileTree = SelectionTree()

        selectionScroll = VisScrollPane(actorTree.tree)
        selectionScroll.setScrollingDisabled(true, false)
        selectionScroll.setFadeScrollBars(false)

        table = VisTable()
        table.add(actorModeButt).fillX()
        table.add(tileModeButt).fillX()
        table.row()

        table.add(selectionScroll).colspan(2).grow()

        buildActorTree()
        buildTileTree()
    }

    private fun buildActorTree(){
        actorTree.addLeaf("Player", Player::class)
        actorTree.addLeaf("Test Object", TestActor::class)

        actorTree.pushBranch("Lights")
        actorTree.addLeaf("Point Light", PointLight::class)
        actorTree.addLeaf("Cone Light", ConeLight::class)
        actorTree.popBranch()
    }

    private fun buildTileTree(){
        tileTree.pushBranch("Floors")
        tileTree.addLeaf("Boarding", FloorTile::class)
        tileTree.popBranch()

        tileTree.pushBranch("Walls")
        tileTree.addLeaf("Wall", WallTile::class)
        tileTree.addLeaf("Glass", GlassTile::class)
        tileTree.popBranch()

        tileTree.pushBranch("Machine")
        tileTree.addLeaf("Door", DoorTile::class)
        tileTree.popBranch()
    }

    fun isActorMode(): Boolean{
        return mode == Mode.ACTORS
    }

    fun isTileMode(): Boolean{
        return mode == Mode.TILES
    }

    fun hasSelectedTile(): Boolean{
        return tileTree.hasSelection && mode == Mode.TILES
    }

    fun hasSelectedActor(): Boolean{
        return actorTree.hasSelection && mode == Mode.ACTORS
    }

    fun buildSelectedActor(): rustyice.game.Actor{
        val type = actorTree.tree.selection.lastSelected.`object`
        if(type is KClass<*>){
            val instance = type.primaryConstructor?.call()
            if(instance is rustyice.game.Actor){
                return instance
            } else {
                throw IllegalStateException("")
            }
        } else {
            throw IllegalStateException("")
        }
    }

    fun buildSelectedTile(): Tile{
        val type = tileTree.tree.selection.lastSelected.`object`
        if(type is KClass<*>){
            val instance = type.primaryConstructor?.call()
            if(instance is rustyice.game.tiles.Tile){
                return instance
            } else {
                throw IllegalStateException("")
            }
        } else {
            throw IllegalStateException("")
        }
    }

    private inner class ModeChangeListener : ChangeListener(){
        override fun changed(event: ChangeEvent, actor: Actor) {
            if(actor == actorModeButt){
                _mode = Mode.ACTORS
                selectionScroll.widget = actorTree.tree
            } else if(actor == tileModeButt){
                _mode = Mode.TILES
                selectionScroll.widget = tileTree.tree
            }
        }
    }

    enum class Mode{
        ACTORS, TILES
    }
}
