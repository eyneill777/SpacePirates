package rustyice.game.physics;

public interface Collidable {
	void beginCollision(Collision collision);
    void endCollision(Collision collision);
}