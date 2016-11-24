package rustyice.editor;

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.utils.Array
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.kotcrab.vis.ui.widget.MenuBar
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter
import ktx.actors.onClick
import ktx.vis.menu
import ktx.vis.menuItem
import ktx.vis.table
import rustyengine.RustyEngine
import rustyice.game.Game
import rustyice.game.Section
import rustyice.levelgenerators.N4JLevelGenerator
import rustyice.screens.Screen
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

/**
 * @author Gabriel Keith
 */
class EditorScreen() : Screen() {
    val game: Game = RustyEngine.game
    val kryo: Kryo = RustyEngine.kryo

    private val root: VisTable

    private val loadFile: FileHandle? = null

    private val propertyPane: EditorPropertyPane
    private val selectionPane: EditorSelectionPane
    private val gameView: EditorGameView

    private fun buildMenuBar(): MenuBar {
        return ktx.vis.menuBar {
            menu("File"){
                menuItem("New")
                menuItem("Save"){
                    onClick { inputEvent: InputEvent, menuItem: MenuItem ->
                        showFileChooser(Mode.OPEN)
                    }
                }
                menuItem("Load"){
                    onClick { inputEvent: InputEvent, menuItem: MenuItem ->
                        showFileChooser(Mode.SAVE)
                    }
                }
                menuItem("Quit"){
                    onClick { inputEvent: InputEvent, menuItem: MenuItem ->
                        screenManager.popScreen()
                    }
                }
            }

            menu("Generators"){
                menuItem("D"){
                    onClick { inputEvent: InputEvent, menuItem: MenuItem ->
                        N4JLevelGenerator().generate(game.currentSection!!)
                    }
                }
            }
        }
    }

    private fun showFileChooser(mode: Mode) {
        if(mode == Mode.OPEN){
            val saveChooser = FileChooser(Mode.SAVE)
            saveChooser.selectionMode = SelectionMode.FILES
            saveChooser.setListener(object: FileChooserAdapter() {
                override fun selected(files: Array<FileHandle>) {
                    saveSection(files.first())
                }
            })
            saveChooser.height = 500f
            saveChooser.width = 400f
            stage.addActor(saveChooser.fadeIn())
        } else{
            val loadChooser = FileChooser(Mode.OPEN)
            loadChooser.selectionMode = SelectionMode.FILES
            loadChooser.setListener(object: FileChooserAdapter() {
                override fun selected(files: Array<FileHandle>) {
                    loadSection(files.first())
                }
            })
            loadChooser.height = 500f
            loadChooser.width = 400f
            stage.addActor(loadChooser.fadeIn())
        }
    }

    private fun saveSection(file: FileHandle) {
        val output = Output(DeflaterOutputStream(file.write(false)))
        kryo.writeObject(output, game.currentSection)
        output.close()
    }

    private fun loadSection(file: FileHandle) {
        val input = Input(InflaterInputStream(file.read()))
        val section = kryo.readObject(input, Section::class.java)
        game.loadSectionNow(section)

        input.close()
    }

    override fun show() {
        stage.addActor(root)
        gameView.display.setSize(stage.width, stage.height)

        game.finishLoadingSection()
    }

    override fun hide(){
        stage.clear()
        gameView.dispose()
    }

    override fun dispose() {
        gameView.dispose()
    }

    override fun resize(width: Int, height: Int) {}

    override fun render(batch: Batch, delta: Float) {
        gameView.render(batch, delta)
    }

    init {
        selectionPane = EditorSelectionPane()
        propertyPane = EditorPropertyPane()
        gameView = EditorGameView(game, selectionPane, propertyPane)
        root = table {
            setFillParent(true)

            add(buildMenuBar().table).growX().row()

            multiSplitPane {
                setWidgets (
                    propertyPane.root,
                    gameView.display,
                    selectionPane.table
                )
                setSplit(0, 0.2f)
                setSplit(1, 0.8f)
            }.grow()
        }
    }
}
