package rustyice.game.physics

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import rustyice.editor.annotations.ComponentProperty
import rustyice.graphics.Camera
import rustyice.physics.Collision
import rustyice.physics.PhysicsComponent

/**
 * Single body physics component
 * @author gabek
 */
open class SBPhysicsComponent: PhysicsComponent() {
    @Transient protected var body: Body? = null
        private set

    var bodyType = BodyType.DynamicBody

    @ComponentProperty
    var flying = false
        set(value) {
            field = value
            body?.let{ it.linearDamping = if(flying) 0f  else it.mass * groundFriction}
        }

    var groundFriction = .5f

    private var _x = 0f
    private var _y = 0f
    private var _rotation = 0f

    override var x: Float
        get() = body?.position?.x ?: _x
        set(value) {
            val body = body
            _x = value
            body?.setTransform(value, body.position.y, body.transform.rotation)
        }

    override var y: Float
        get() = body?.position?.y ?: _y
        set(value) {
            val body = body
            _y = value
            body?.setTransform(body.position.x, value, body.angle)
        }

    override var rotation: Float
        get() = (body?.transform?.rotation ?: _rotation) * MathUtils.radDeg
        set(value) {
            val body = body
            _rotation = value
            body?.setTransform(body.position, value * MathUtils.degRad)
        }

    override fun init() {
        super.init()
        val world = section!!.game!!.world

        val bodyDef = buildBodyDef()

        body = world.createBody(bodyDef)
    }
    
    private fun updateDrag() {
        val body = body

        if (isInitialized && !flying && body != null) {
            body.linearDamping = body.mass * groundFriction
            body.angularDamping = body.mass * groundFriction
        }
    }

    override fun update(delta: Float) {
        val body = body

        if(body != null) {
            if (!flying && Math.abs(body.linearVelocity.x) < .1f && Math.abs(body.linearVelocity.y) < .1f && !body.linearVelocity.isZero) {
                _x = body.position.x
                _y = body.position.y
                _rotation = body.transform.rotation

                body.linearVelocity = Vector2.Zero
            }
        }
    }

    override fun beginCollision(collision: Collision) {}

    override fun endCollision(collision: Collision) {}

    override fun store() {
        super.store()
        val body = body

        if(body != null){
            parent!!.section!!.game!!.world.destroyBody(body)
            this.body = null
        }
    }

    protected fun buildBodyDef(): BodyDef {
        val bodyDef = BodyDef()

        
        bodyDef.type = bodyType
        // bodyDef.fixedRotation = true;

        bodyDef.position.x = _x
        bodyDef.position.y = _y
        bodyDef.angle = _rotation

        return bodyDef
    }
    
    fun addRectangle(width: Float, height: Float, fixtureDef: FixtureDef): Fixture {
        val body = body!!

        val rect = PolygonShape()
        rect.setAsBox(width / 2, height / 2)

        fixtureDef.shape = rect
        val fix = body.createFixture(fixtureDef)
        rect.dispose()

        fix.userData = parent
        updateDrag()
        return fix
    }

    fun addCircle(radius: Float, fixtureDef: FixtureDef): Fixture {
        val body = body!!

        val circle = CircleShape()
        circle.radius = radius

        fixtureDef.shape = circle
        val fix = body.createFixture(fixtureDef)
        circle.dispose()

        fix.userData = parent
        updateDrag()
        return fix
    }

    override fun render(batch: Batch, camera: Camera) {}
}
