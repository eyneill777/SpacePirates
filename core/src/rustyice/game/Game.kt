package rustyice.game

import box2dLight.RayHandler
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import rustyice.graphics.Camera
import rustyice.input.PlayerInput
import rustyice.physics.Collidable
import rustyice.physics.Collision
import rustyengine.resources.Resources
import java.util.*

class Game: ContactListener {
    private val UPDATE_RATE = 1/60f

    val lightingHandler: RayHandler

    val playerInputs: MutableList<PlayerInput>
    val cameras: MutableList<Camera>
    val povHandlers: MutableList<RayHandler>

    val world: World



    var currentSection: Section? = null
        private set

    private var sectionToLoad: Section? = null
    private val collisions: ArrayList<Collision>
    private var leftOverTime: Float = 0f

    constructor() {
        world = World(Vector2.Zero, true)
        world.setContactListener(this)

        lightingHandler = RayHandler(world)
        playerInputs = ArrayList()
        cameras = ArrayList()
        povHandlers = ArrayList()

        sectionToLoad = Section()

        // actors = new ArrayList<>();
        collisions = ArrayList()
    }

    fun startLoadingSection(section: Section){
        sectionToLoad = section
    }

    fun loadSectionNow(section: Section){
        startLoadingSection(section)
        finishLoadingSection()
    }

    fun finishLoadingSection() {
        val section = sectionToLoad

        if(section != null) {
            currentSection?.store()

            section.game = this
            section.init()

            currentSection = section
            sectionToLoad = null
        }
    }

    fun update(delta: Float) {
        finishLoadingSection()
        
        leftOverTime += delta

        while (leftOverTime > 0) {
            leftOverTime -= UPDATE_RATE

            world.step(UPDATE_RATE, 6, 2)

            for (col in collisions) {
                if (col.isBegin) {
                    resolveCollidable(col.thisFixture)?.beginCollision(col)
                } else {
                    resolveCollidable(col.thisFixture)?.endCollision(col)
                }
            }
            collisions.clear()

            currentSection?.update(UPDATE_RATE)
        }
    }

    fun render(batch: Batch, camera: Camera, renderFlags: Int) {
        currentSection?.render(batch, camera, renderFlags)
    }

    fun dispose() {
        currentSection?.store()

        world.dispose()
        lightingHandler.dispose()
    }

    fun resolveCollidable(fixture: Fixture): Collidable? {
        return fixture.userData as Collidable?
    }

    override fun beginContact(contact: Contact) {
        collisions.add(Collision(contact.fixtureA, contact.fixtureB, true))
        collisions.add(Collision(contact.fixtureB, contact.fixtureA, true))
    }

    override fun endContact(contact: Contact) {
        collisions.add(Collision(contact.fixtureA, contact.fixtureB, false))
        collisions.add(Collision(contact.fixtureB, contact.fixtureA, false))
    }

    override fun preSolve(contact: Contact, manifold: Manifold) {}
    override fun postSolve(contact: Contact, contactImpulse: ContactImpulse) {}
}