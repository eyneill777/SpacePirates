package spacepirates.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

public interface Collidable {
	void beginCollision(Fixture thisFixture, Fixture otherFixture, Contact contact);
	void endCollision(Fixture thisFixture, Fixture otherFixture, Contact contact);
}
