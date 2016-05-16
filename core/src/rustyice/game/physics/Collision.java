package rustyice.game.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Collision {

    private Fixture thisFixture;
    private Fixture otherFixture;
    private boolean begin;

    public Collision(Fixture thisFixture, Fixture otherFixture, Contact contact, boolean begin) {
        this.thisFixture = thisFixture;
        this.otherFixture = otherFixture;
        this.begin = begin;
    }

    public Collidable getOther() {
        return (Collidable) otherFixture.getUserData();
    }

    public Fixture getThisFixture() {
        return thisFixture;
    }

    public Fixture getOtherFixture() {
        return otherFixture;
    }

    public boolean isBegin() {
        return begin;
    }
}
