package rustyengine.physics

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Disposable
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.KryoSerializable
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import java.util.*

/**
 * @author Gabriel Keith
 */

class RBody : KryoSerializable, Disposable{
    private var body: Body? = null
    private val fixutres: MutableList<RFixture> = ArrayList()

    var x: Float = 0f
        get() = body?.position?.x ?: field
        set(value) {
            val body = body
            if(body != null)
                body.setTransform(value, body.position.y, body.angle)
            else
                field = value
        }

    var y: Float = 0f
        get() = body?.position?.y ?: field
        set(value) {
            val body = body
            if(body != null)
                body.setTransform(body.position.x, value, body.angle)
            else
                field = value
        }

    var rotationRad: Float = 0f
        get() = body?.angle ?: field
        set(value) {
            val body = body
            if(body != null)
                body.setTransform(body.position, value)
            else
                field = value
        }

    var rotation: Float
        get() = rotationRad * MathUtils.radDeg
        set(value) {
            rotationRad = value * MathUtils.degRad
        }

    var angularDamping: Float = 0f
        get() = body?.angularDamping ?: field
        set(value) {
            val body = body
            if(body != null)
                body.angularDamping = value
            else
                field = value
        }

    var angularVelocity: Float = 0f
        get() = body?.angularVelocity ?: field
        set(value) {
            val body = body
            if(body != null)
                body.angularVelocity = value
            else
                field = value
        }

    var linearDamping: Float = 0f
        get() = body?.linearDamping ?: field
        set(value) {
            val body = body
            if(body != null)
                body.linearDamping = value
            else
                field = value
        }

    var linearVelocityX: Float = 0f
        get() = body?.linearVelocity?.x ?: field
        set(value) {
            val body = body
            if(body != null)
                body.setLinearVelocity(value, body.linearVelocity.y)
            else
                field = value
        }

    var linearVelocityY: Float = 0f
        get() = body?.linearVelocity?.y ?: field
        set(value) {
            val body = body
            if(body != null)
                body.setLinearVelocity(body.linearVelocity.x, value)
            else
                field = value
        }

    var gravityScale: Float = 0f
        get() = body?.gravityScale ?: field
        set(value) {
            val body = body
            if(body != null)
                body.gravityScale = value
            else
                field = value
        }

    var isActive: Boolean = true
        get() = body?.isActive ?: field
        set(value) {
            val body = body
            if(body != null)
                body.isActive = value
            else
                field = value
        }

    var isAwake: Boolean = true
        get() = body?.isAwake ?: field
        set(value) {
            val body = body
            if(body != null)
                body.isAwake = value
            else
                field = value
        }

    var isSleepingAllowed: Boolean = true
        get() = body?.isSleepingAllowed ?: field
        set(value) {
            val body = body
            if(body != null)
                body.isSleepingAllowed = value
            else
                field = value
        }

    var isBullet: Boolean = false
        get() = body?.isBullet ?: field
        set(value) {
            val body = body
            if(body != null)
                body.isBullet = value
            else
                field = value
        }

    var isFixedRotation: Boolean = false
        get() = body?.isFixedRotation ?: field
        set(value) {
            val body = body
            if(body != null)
                body.isFixedRotation = value
            else
                field = value
        }

    var bodyType: BodyDef.BodyType = BodyDef.BodyType.StaticBody
        get() = body?.type ?: field
        set(value) {
            val body = body
            if(body != null)
                body.type = value
            else
                field = value
        }

    val isInitialised: Boolean
        get() = body != null

    fun initialise(box2dWorld: World){
        body = box2dWorld.createBody(bodyDef)
    }

    override fun dispose() {
        val body = body
        if(body != null){
            val world = body.world
            world.destroyBody(body)
            this.body = null
        }
    }

    override fun read(kryo: Kryo, input: Input) {
        x = input.readFloat()
        y = input.readFloat()
        rotationRad = input.readFloat()

        linearVelocityX = input.readFloat()
        linearVelocityY = input.readFloat()
        angularVelocity = input.readFloat()
    }

    override fun write(kryo: Kryo, output: Output) {
        output.writeFloat(x)
        output.writeFloat(y)
        output.writeFloat(rotationRad)

        output.writeFloat(linearVelocityX)
        output.writeFloat(linearVelocityY)
        output.writeFloat(angularVelocity)
    }

    private val bodyDef: BodyDef get() {
        val def = BodyDef()

        def.position.x = x
        def.position.y = y
        def.angle = rotationRad

        def.angularDamping = angularDamping
        def.angularVelocity = angularVelocity

        def.linearDamping = linearDamping
        def.linearVelocity.set(linearVelocityX, linearVelocityY)

        def.gravityScale = gravityScale

        def.active = isActive
        def.awake = isAwake
        def.allowSleep = isSleepingAllowed
        def.bullet = isBullet
        def.fixedRotation = isFixedRotation
        def.type = bodyType

        return def
    }
}