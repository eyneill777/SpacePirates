package spacepirates.game.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

public interface Collidable {
	void beginCollision(Fixture thisFixture, Fixture otherFixture, Contact contact);
	void endCollision(Fixture thisFixture, Fixture otherFixture, Contact contact);
	boolean shouldCollide(Fixture thisFixture, Fixture otherFixture);
}