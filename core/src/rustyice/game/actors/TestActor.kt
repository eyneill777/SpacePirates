package rustyice.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.FixtureDef
import rustyengine.RustyEngine
import rustyice.game.Actor
import rustyice.game.physics.SBPhysicsComponent
import rustyice.graphics.Camera
import rustyice.graphics.RenderLayer
import rustyice.physics.LARGE
import rustyice.physics.WALL

/**
 * @author gabek
 */
class TestActor() : Actor() {
    @Transient private var testSprite: Sprite? = null

    val sbPhysicsComponent: SBPhysicsComponent

    override fun update(delta: Float) {
        super.update(delta)
    }

    override fun render(batch: Batch, camera: Camera, layer: RenderLayer) {
        val testSprite = testSprite
        if(testSprite != null){
            testSprite.x = x - width / 2
            testSprite.y = y - height / 2
            testSprite.rotation = rotation
            testSprite.draw(batch)
        }
    }

    override fun init() {
        super.init()

        val fixtureDef = FixtureDef()
        fixtureDef.density = 1f
        fixtureDef.filter.categoryBits = LARGE.toShort()
        fixtureDef.filter.maskBits = (LARGE or
                                      WALL).toShort()
        sbPhysicsComponent.addRectangle(width, height, fixtureDef)


        val testSprite = Sprite(RustyEngine.resorces.box)
        this.testSprite = testSprite

        testSprite.setSize(width, height)
        testSprite.setOrigin(width/2, height/2)
    }

    init {
        sbPhysicsComponent = SBPhysicsComponent()
        physicsComponent = sbPhysicsComponent
        width = 1.9f
        height = 1.9f
    }
}
