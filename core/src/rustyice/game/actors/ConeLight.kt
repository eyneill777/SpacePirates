package rustyice.game.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import rustyengine.RustyEngine
import rustyice.editor.annotations.ComponentAccess
import rustyice.game.Actor
import rustyice.game.lights.ConeLightComponent
import rustyice.game.lights.LightComponent
import rustyice.graphics.Camera
import rustyice.graphics.EDITOR
import rustyengine.resources.Resources

/**
 * @author gabek
 */
class ConeLight() : Actor(), LightComponent.LightContainer {
    @Transient private var icon: Sprite? = null
    @ComponentAccess
    override val lightComponent = ConeLightComponent()

    override var x: Float
        get() = super.x
        set(value) {
            super.x = value
            lightComponent.x = value
        }

    override var y: Float
        get() = super.y
        set(value) {
            super.y = value
            lightComponent.y = value
        }

    override var rotation: Float
        get() = super.rotation
        set(value) {
            super.rotation = value
            lightComponent.direction = value
        }

    override fun render(batch: Batch, camera: Camera, renderFlags: Int) {
        lightComponent.render(batch, camera, renderFlags)

        if((renderFlags and EDITOR) == EDITOR){
            val icon = icon ?: RustyEngine.resorces.gameArt.createSprite("EditorLight")
            this.icon = icon

            icon.setBounds(x - width/2, y - height/2, width, height)
            icon.setOriginCenter()
            icon.rotation = rotation
            icon.color = Color.YELLOW
            icon.draw(batch)
        }
    }

    override fun init() {
        super.init()
        lightComponent.setPosition(x, y)
        lightComponent.direction = rotation
        lightComponent.parent = this
        lightComponent.init()
    }

    override fun update(delta: Float) {
        super.update(delta)
        lightComponent.update(delta)
    }

    override fun store() {
        super.store()
        lightComponent.store()
    }

    init {
        setSize(1f, 1f)
    }
}
