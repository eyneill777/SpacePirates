package rustyice.game.physics

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import rustyice.game.MissingParentReference
import rustyice.game.tiles.TILE_SIZE
import rustyice.graphics.Camera
import rustyice.physics.*

/**
 * @author Gabriel Keith
 */
class DoorPhysicsComponent: TilePhysicsComponent() {
    val SIZE_FACTOR = 1.1f

    @Transient private var sensorBody: Body? = null
    @Transient private var doorLeftBody: Body? = null
    @Transient private var jointLeft: PrismaticJoint? = null

    override var x = 0f
    override var y = 0f
    override var rotation = 0f
        set(value) {
            field = value
            if(isInitialized){
                store()
                init()
            }
        }

    private var percentOpen = 0f
    var targetPercentOpen = percentOpen
    var moterSpeed = 5f

    private var clock = 0f

    override fun init() {
        super.init()

        val parentTile = parentTile
            ?: throw MissingParentReference("Tile")

        val world = parentTile.section?.game?.world
                ?: throw MissingParentReference("Game")


        val sensorBodyDef = BodyDef()
        sensorBodyDef.type = BodyDef.BodyType.StaticBody
        sensorBodyDef.angle = MathUtils.degRad * rotation
        sensorBodyDef.position.set(x + TILE_SIZE/2, y + TILE_SIZE/2)
        val sensorBody = world.createBody(sensorBodyDef)
        this.sensorBody = sensorBody

        val sensorFixtureDef = FixtureDef()
        sensorFixtureDef.isSensor = true
        sensorFixtureDef.filter.categoryBits = ACTIVATABLE.toShort()
        sensorFixtureDef.filter.maskBits = ACTIVATOR.toShort()

        val polyShape = PolygonShape()
        polyShape.setAsBox(TILE_SIZE/2, TILE_SIZE/2)
        sensorFixtureDef.shape = polyShape
        sensorBody.createFixture(sensorFixtureDef)
        polyShape.dispose()

        val doorLeftBody = createDoorPanelBody(world)
        this.doorLeftBody = doorLeftBody

        createDoorPanelOuter(doorLeftBody)

        jointLeft = createHingJoint(world, sensorBody, doorLeftBody)
    }

    private fun createDoorPanelBody(world: World): Body{
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.angle = MathUtils.degRad * rotation
        bodyDef.position.set(x + TILE_SIZE/2, y + TILE_SIZE/2)
        return world.createBody(bodyDef)
    }

    private fun createDoorPanelOuter(parentBody: Body): Fixture{
        val polygonShape = PolygonShape()
        polygonShape.setAsBox(TILE_SIZE/2, TILE_SIZE/8)

        val fixtureDef = FixtureDef()

        fixtureDef.shape = polygonShape
        fixtureDef.filter.categoryBits = LARGE.toShort()
        fixtureDef.filter.maskBits = (LARGE or SMALL).toShort()
        val fixture = parentBody.createFixture(fixtureDef)
        polygonShape.dispose()

        return fixture
    }

    private fun createHingJoint(world: World, base: Body, door: Body): PrismaticJoint{
        val jointDef = PrismaticJointDef()

        jointDef.bodyA = base
        jointDef.bodyB = door

        jointDef.enableLimit = true
        jointDef.upperTranslation = 0f
        jointDef.lowerTranslation = -TILE_SIZE

        jointDef.maxMotorForce = 5f
        jointDef.enableMotor = true
        //jointDef.motorSpeed = 1;
        //jointDef.maxMotorForce = 1;
        return world.createJoint(jointDef) as PrismaticJoint
    }

    override fun store() {
        super.store()
        val world = parent?.game?.world
            ?: throw MissingParentReference("Game")

        world.destroyJoint(jointLeft)
        jointLeft = null

        world.destroyBody(sensorBody)
        sensorBody = null

        world.destroyBody(doorLeftBody)
        doorLeftBody = null
    }

    override fun update(delta: Float) {
        val jointLeft = jointLeft

        if(jointLeft != null) {
            percentOpen = jointLeft.jointTranslation / jointLeft.lowerLimit
            if (Math.abs(percentOpen - targetPercentOpen) < 0.05f) {
                jointLeft.motorSpeed = 0f
            } else {
                if (percentOpen > targetPercentOpen) {
                    jointLeft.motorSpeed = moterSpeed
                } else {
                    jointLeft.motorSpeed = -moterSpeed
                }
            }
        }

        clock += delta
        targetPercentOpen = if(clock.toInt() % 2 == 0) 0f else 1f
    }

    override fun render(batch: Batch, camera: Camera) {}

    override fun beginCollision(collision: Collision) {}
    override fun endCollision(collision: Collision) {}
}
