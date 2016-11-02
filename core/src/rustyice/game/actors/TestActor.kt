package rustyice.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.FixtureDef
import rustyice.game.Actor
import rustyice.game.physics.SBPhysicsComponent
import rustyice.graphics.Camera
import rustyice.graphics.NORMAL
import rustyice.physics.LARGE
import rustyice.physics.LIGHT
import rustyice.physics.OPAQUE
import rustyice.physics.WALL
import rustyice.resources.Resources

/**
 * @author gabek
 */
class TestActor : Actor {
    @Transient private var testSprite: Sprite? = null

    val sbPhysicsComponent: SBPhysicsComponent

    constructor(): super(){
        sbPhysicsComponent = SBPhysicsComponent()
        physicsComponent = sbPhysicsComponent

        width = 1.9f
        height = 1.9f
    }

    override fun update(delta: Float) {
        super.update(delta)
    }

    override fun render(batch: Batch, camera: Camera, renderFlags: Int) {
        if((renderFlags and NORMAL) == NORMAL){
            val testSprite = testSprite
            if(testSprite != null){
                testSprite.x = x - width / 2
                testSprite.y = y - height / 2
                testSprite.rotation = rotation
                testSprite.draw(batch)
            }
        }
    }

    override fun init() {
        super.init()

        val fixtureDef = FixtureDef()
        fixtureDef.density = 1f
        fixtureDef.filter.categoryBits = (LARGE or OPAQUE).toShort()
        fixtureDef.filter.maskBits = (LARGE or
                                      WALL or
                                      LIGHT).toShort()
        sbPhysicsComponent.addRectangle(width, height, fixtureDef)


        val testSprite = Sprite(Resources.box)
        this.testSprite = testSprite

        testSprite.setSize(width, height)
        testSprite.setOrigin(width/2, height/2)
    }
}
