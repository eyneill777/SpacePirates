package spacepirates.game.physics;

import com.badlogic.gdx.physics.box2d.Fixture;

public class Collision {
	public Fixture thisFixture;
	public Fixture otherFixture;

	public Collidable getOther(){
		return (Collidable) otherFixture.getBody().getUserData();
	}
}
