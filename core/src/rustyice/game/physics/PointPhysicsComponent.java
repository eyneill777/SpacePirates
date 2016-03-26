package rustyice.game.physics;

public class PointPhysicsComponent implements PhysicsComponent {
	private float x, y, rotation;
	
	public PointPhysicsComponent() {
		x = 0;
		y = 0;
		rotation = 0;
	}
	
	public PointPhysicsComponent(float x, float y) {
		this.x = x;
		this.y = y;
		rotation = 0;
	}
	
	public PointPhysicsComponent(float x, float y, float rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}
	
	
	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	@Override
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void init() {}

	@Override
	public void store() {}

	@Override
	public void update(float delta) {}

	@Override
	public void beginCollision(Collision collision) {}

	@Override
	public void endCollision(Collision collision) {}
}
