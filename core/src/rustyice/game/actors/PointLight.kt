package rustyice.game.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import rustyice.editor.annotations.ComponentAccess
import rustyice.game.Actor
import rustyice.game.lights.LightComponent
import rustyice.game.lights.PointLightComponent
import rustyice.graphics.Camera
import rustyice.graphics.EDITOR
import rustyice.resources.Resources


/**
 * @author gabek
 */
class PointLight : Actor, LightComponent.LightContainer {
    @Transient private var icon: Sprite? = null
    override val lightComponent = PointLightComponent()
        @ComponentAccess get

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

    constructor(): super() {
        setSize(1f, 1f)
    }

    override fun render(batch: Batch, camera: Camera, renderFlags: Int) {
        lightComponent.render(batch, camera, renderFlags)

        if ((renderFlags or EDITOR) == EDITOR) {
            if (icon == null) {
                icon = Resources.gameArt.createSprite("EditorLight")
            }

            icon?.let {
                it.setBounds(x - width / 2, y - height / 2, width, height)
                it.setOriginCenter()
                it.rotation = rotation
                it.color = Color.YELLOW
                it.draw(batch)
            }
        }
    }

    override fun init() {
        super.init()
        lightComponent.setPosition(x, y)
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
}
