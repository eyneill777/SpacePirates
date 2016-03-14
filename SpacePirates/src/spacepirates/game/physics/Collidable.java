package spacepirates.game.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import spacepirates.game.GameObject;

public interface Collidable {
	void beginCollision(Collision collision);
    void endCollision(Collision collision);
	boolean shouldCollide(Fixture thisFixture, Fixture otherFixture);
}