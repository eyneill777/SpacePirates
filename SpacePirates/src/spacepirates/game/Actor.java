package spacepirates.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import spacepirates.game.physics.Collidable;
import spacepirates.game.physics.PhysicsComponent;
import spacepirates.resources.Resources;

public abstract class Actor implements GameObject, Collidable{
	private Game game;
	private boolean initialized = false;
	private PhysicsComponent physicsComponent;
    private float width, height;

	public float getX(){
		return physicsComponent.getX();
	}
	
	public float getY(){
		return physicsComponent.getY();
	}
	
	public void setX(float x){
		physicsComponent.setX(x);
	}
	
	public void setY(float y){
		physicsComponent.setY(y);
	}
	
	public float getWidth(){
		return this.width;
	}
	
	public float getHeight(){
		return this.height;
	}
	
	public void setWidth(float width){
		this.width = width;
	}
	
	public void setHeight(float height){
		this.height = height;
	}
	
	public void setSize(float width, float height){
		this.width = width;
		this.height = height;
	}
	
	public void setPosition(float x, float y){
		physicsComponent.setPosition(x, y);
	}
	
	public float getRotation(){
		return physicsComponent.getRotation();
	}
	
	public void setRotation(float rotation){
		physicsComponent.setRotation(rotation);
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public Game getGame(){
		return game;
	}
	
	public Resources getResources(){
		return game.getResources();
	}

    @Override
	public void init(){
		initialized = true;
        physicsComponent.init();
	}

    @Override
	public void store(){
		initialized = false;
        physicsComponent.store();
	}

    @Override
    public void update(float delta) {
        physicsComponent.update(delta);
    }

    public PhysicsComponent getPhysicsComponent() {
        return physicsComponent;
    }

    public void setPhysicsComponent(PhysicsComponent physicsComponent) {
        this.physicsComponent = physicsComponent;
    }

    public boolean isInitialized(){
		return initialized;
	}

    @Override
    public void collision(Fixture thisFixture, Fixture otherFixture, Contact contact) {
        physicsComponent.collision(thisFixture, otherFixture, contact);
    }

    @Override
    public boolean shouldCollide(Fixture thisFixture, Fixture otherFixture) {
        return true;
    }
}
