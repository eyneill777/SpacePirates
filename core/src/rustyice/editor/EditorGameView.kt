package rustyice.editor

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.github.salomonbrys.kodein.instance
import rustyengine.RustyEngine
import rustyice.game.Actor
import rustyice.game.Game
import rustyice.game.tiles.TILE_SIZE
import rustyice.graphics.Camera
import rustyice.graphics.GameDisplay
import rustyice.graphics.drawRect

class EditorGameView(val game: Game, val selectionPane: EditorSelectionPane, val propertyPane: EditorPropertyPane){
    val display: GameDisplay

    private val editorInput: EditorInput
    private val camera: Camera

    private var mouseDraging: Boolean = false
    private val mouseDownPos: Vector2 = Vector2()
    private val mouseDraggedPos: Vector2 = Vector2()
    private var selectionActor: Actor? = null
    private val selectionDownPos: Vector2 = Vector2()

    private val tileSelectStart: Vector2 = Vector2()
    private val tileSelectEnd: Vector2 = Vector2()

    init {
        camera = Camera()
        camera.width = 20f
        camera.height = 20f

        display = GameDisplay()
        display.game = game
        display.camera = camera

        editorInput = EditorInput()
        display.addListener(editorInput)
    }

    fun render(batch: Batch, delta: Float) {
        game.currentSection?.finishAddingActors()

        if (editorInput.moveUp) {
            camera.y += camera.height * delta
        }
        if (editorInput.moveDown) {
            camera.y -= camera.height * delta
        }
        if (editorInput.moveLeft) {
            camera.x -= camera.width * delta
        }
        if (editorInput.moveRight) {
            camera.x += camera.width * delta
        }

        if (editorInput.zoomIn) {
            camera.width *= (1 - 0.75f * delta)
            camera.height *= (1 - 0.75f * delta)
        }
        if (editorInput.zoomOut) {
            camera.width *= (1 + 0.75f * delta)
            camera.height *= (1 + 0.75f * delta)
        }

        display.render(batch)

        if (selectionPane.isTileMode() && mouseDraging) {
            display.fbo!!.begin()
            batch.begin()

            batch.color = Color.LIGHT_GRAY
            batch.drawRect(
                    tileSelectStart.x * TILE_SIZE,
                    tileSelectStart.y  * TILE_SIZE,
                    (tileSelectEnd.x - tileSelectStart.x)  * TILE_SIZE,
                    (tileSelectEnd.y - tileSelectStart.y)  * TILE_SIZE, 0.1f
            )

            batch.end()
            display.fbo!!.end()
        }
    }

    fun dispose(){
        display.dispose()
    }

    private inner class EditorInput: ClickListener(){
        var moveUp = false
        var moveDown = false
        var moveLeft = false
        var moveRight = false
        var zoomIn = false
        var zoomOut = false


        override fun keyDown(event: InputEvent, keycode: Int):Boolean {
            when(keycode){
                Input.Keys.UP -> moveUp = true
                Input.Keys.DOWN -> moveDown = true
                Input.Keys.LEFT -> moveLeft = true
                Input.Keys.RIGHT -> moveRight = true
                Input.Keys.Z -> zoomIn = true
                Input.Keys.X -> zoomOut = true
                Input.Keys.R -> {
                        selectionActor?.let{ game.currentSection?.actors?.remove(it) }
                        selectionActor?.store()
                        selectionActor = null
                }
                else -> return false
            }
            return true
        }

        override fun keyUp(event: InputEvent, keycode: Int): Boolean {
            when (keycode) {
                Input.Keys.UP -> moveUp = false
                Input.Keys.DOWN -> moveDown = false
                Input.Keys.LEFT -> moveLeft = false
                Input.Keys.RIGHT -> moveRight = false
                Input.Keys.Z -> zoomIn = false
                Input.Keys.X -> zoomOut = false
                else -> return false
            }
            return true
        }

        override fun clicked(event: InputEvent, x: Float, y: Float) {
            super.clicked(event, x, y)
            event.stage.keyboardFocus = event.listenerActor

            if (selectionPane.isActorMode() && selectionPane.hasSelectedActor()) {
                val actor = selectionPane.buildSelectedActor()
                actor.setPosition(mouseDownPos.x, mouseDownPos.y)
                game.currentSection?.addActor(actor)
            }
        }

        override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            super.touchDown(event, x, y, pointer, button)

            mouseDownPos.set(x, y)
            display.stageToSection(mouseDownPos)

            val section = game.currentSection
            if(section != null) {
                if (selectionPane.isActorMode()) {
                    for (actor in section.actors) {
                        if (mouseDownPos.x >= actor.x - actor.width / 2 && mouseDownPos.y >= actor.y - actor.height / 2
                                && mouseDownPos.x < actor.x + actor.width / 2 && mouseDownPos.y < actor.y + actor.height / 2) {
                            selectionActor = actor
                            selectionDownPos.set(actor.x, actor.y)
                            propertyPane.setSelected(actor)
                            break
                        }
                    }
                } else {
                    val tile = section.tiles.getTileAt(mouseDownPos.x, mouseDownPos.y)
                    if (tile != null) {
                        propertyPane.setSelected(tile)
                    }
                }
            }

            return true
        }

        override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
            super.touchUp(event, x, y, pointer, button)
            selectionActor = null

            val section = game.currentSection

            if (section != null && selectionPane.hasSelectedTile() && mouseDraging) {
                for (i in tileSelectStart.y.toInt() until tileSelectEnd.y.toInt()) {
                    for (j in tileSelectStart.x.toInt() until tileSelectEnd.x.toInt()) {
                        section.tiles.setTile(selectionPane.buildSelectedTile(), j, i)
                    }
                }
            }

            mouseDraging = false
        }

        override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
            super.touchDragged(event, x, y, pointer)

            mouseDraging = true
            mouseDraggedPos.set(x, y)
            display.stageToSection(mouseDraggedPos)

            if (selectionPane.isTileMode()) {
                if (mouseDownPos.x < mouseDraggedPos.x) {
                    tileSelectStart.x = tileFloor(mouseDownPos.x)
                    tileSelectEnd.x = tileCeil(mouseDraggedPos.x)
                } else {
                    tileSelectStart.x = tileFloor(mouseDraggedPos.x)
                    tileSelectEnd.x = tileCeil(mouseDownPos.x)
                }

                if (mouseDownPos.y < mouseDraggedPos.y) {
                    tileSelectStart.y = tileFloor(mouseDownPos.y)
                    tileSelectEnd.y = tileCeil(mouseDraggedPos.y)
                } else {
                    tileSelectStart.y = tileFloor(mouseDraggedPos.y)
                    tileSelectEnd.y = tileCeil(mouseDownPos.y)
                }
            }

            selectionActor?.let {
                it.x = selectionDownPos.x + mouseDraggedPos.x - mouseDownPos.x
                it.y = selectionDownPos.y + mouseDraggedPos.y - mouseDownPos.y
            }
        }

        private fun tileFloor(value: Float): Float{
            return MathUtils.floor(value / TILE_SIZE).toFloat()
        }

        private fun tileCeil(value: Float): Float{
            return MathUtils.ceil(value / TILE_SIZE).toFloat()
        }
    }
}
