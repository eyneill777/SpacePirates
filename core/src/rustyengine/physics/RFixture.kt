package rustyengine.physics

import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.utils.Disposable
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.KryoSerializable
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output

/**
 * @author Gabriel Keith
 */
class RFixture : KryoSerializable, Disposable{
    var fixture: Fixture? = null

    var density: Float = 0f
        get() = fixture?.density ?: field
        set(value) {
            val fixture = fixture
            if(fixture != null)
                fixture.density = value
            else
                field = value
        }

    var friction: Float = 0f
        get() = fixture?.friction ?: field
        set(value) {
            val fixture = fixture
            if(fixture != null)
                fixture.friction = value
            else
                field = value
        }

    var restitution: Float = 0f
        get() = fixture?.restitution ?: field
        set(value) {
            val fixture = fixture
            if(fixture != null)
                fixture.restitution = value
            else
                field = value
        }

    var isSensor: Boolean = false
        get() = fixture?.isSensor ?: field
        set(value) {
            val fixture = fixture
            if(fixture != null)
                fixture.isSensor = value
            else
                field = value
        }

    var categoryBits: Short = 1
        get() = fixture?.filterData?.categoryBits ?: field
        set(value) {
            val fixture = fixture
            if(fixture != null)
                fixture.filterData.categoryBits = value
            else
                field = value
        }

    var maskBits: Short = -1
        get() = fixture?.filterData?.maskBits ?: field
        set(value) {
            val fixture = fixture
            if(fixture != null)
                fixture.filterData.maskBits = value
            else
                field = value
        }

    var groupIndex: Short = 0
        get() = fixture?.filterData?.groupIndex ?: field
        set(value) {
            val fixture = fixture
            if(fixture != null)
                fixture.filterData.groupIndex = value
            else
                field = value
        }

    override fun write(kryo: Kryo, output: Output) {

    }

    override fun read(kryo: Kryo, input: Input) {

    }

    override fun dispose() {
        val fixture = this.fixture
        this.fixture = null
        if(fixture != null){
            val body = fixture.body
            body.destroyFixture(fixture)
        }
    }

    private val fixtureDef: FixtureDef get() {
        val fixtureDef = FixtureDef()

        fixtureDef.density = density
        fixtureDef.friction = friction
        fixtureDef.restitution = restitution
        fixtureDef.isSensor = isSensor
        fixtureDef.filter.categoryBits = categoryBits
        fixtureDef.filter.maskBits = maskBits
        fixtureDef.filter.groupIndex = groupIndex

        return fixtureDef
    }
}