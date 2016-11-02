package rustyice.physics;

import com.badlogic.gdx.physics.box2d.Fixture

class Collision(val thisFixture: Fixture,
                val otherFixture: Fixture,
                val isBegin: Boolean)
