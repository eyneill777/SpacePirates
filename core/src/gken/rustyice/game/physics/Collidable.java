package gken.rustyice.game.physics;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface Collidable {
	void beginCollision(Collision collision);
    void endCollision(Collision collision);
	boolean shouldCollide(Fixture thisFixture, Fixture otherFixture);
}