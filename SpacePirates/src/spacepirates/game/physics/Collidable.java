package spacepirates.game.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import spacepirates.game.GameObject;

public interface Collidable {
	void collision(Fixture thisFixture, Fixture otherFixture, Contact contact);
	boolean shouldCollide(Fixture thisFixture, Fixture otherFixture);
}