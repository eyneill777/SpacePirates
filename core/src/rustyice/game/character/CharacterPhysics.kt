package rustyice.game.character

import com.badlogic.gdx.physics.box2d.FixtureDef
import rustyice.game.Actor
import rustyice.game.physics.SBPhysicsComponent
import rustyice.physics.LARGE
import rustyice.physics.WALL

/**
 * @author gabek
 */
class CharacterPhysics: SBPhysicsComponent(){
    private var characterRadius = 0f
    private var activatorRadius = 0f

    fun walk(maxX: Float, maxY: Float, acceleration: Float) {
        val body = body

        if(body != null) {
            var diffX = maxX - body.linearVelocity.x
            var diffY = maxY - body.linearVelocity.y
            if (Math.abs(diffX) > .1f || Math.abs(diffY) > .1f) {
                // cap on force generated.
                if (Math.abs(diffX) > acceleration) {
                    diffX = if (diffX < 0) -acceleration else acceleration
                }
                if (Math.abs(diffY) > acceleration) {
                    diffY = if (diffY < 0) -acceleration else acceleration
                }

                body.applyLinearImpulse(diffX, diffY, body.worldCenter.x, body.worldCenter.y, true)
            }
        }
    }

    override fun init() {
        super.init()
        val parent = parent!!

        val characterDef = FixtureDef()
        characterDef.density = 1f
        characterDef.filter.categoryBits = LARGE.toShort()
        characterDef.filter.maskBits = (LARGE or WALL).toShort()

        if(parent is Actor){
            addCircle(parent.width/2, characterDef)
        }

        //FixtureDef activatorDef = new FixtureDef();
        //activatorDef.density = 0;
        //characterDef.filter.categoryBits = FillterFlags.ACTIVATOR;
        //characterDef.filter.maskBits = FillterFlags.ACTIVATABLE;

        //addCircle(activatorRadius, activatorDef);

    }
}
