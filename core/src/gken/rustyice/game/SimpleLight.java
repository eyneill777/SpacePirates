package gken.rustyice.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import box2dLight.PointLight;
import gken.rustyice.game.physics.PointPhysicsComponent;

public class SimpleLight extends Actor {
	private PointLight pointLight;
	
	public SimpleLight() {
		setPhysicsComponent(new PointPhysicsComponent(7, 7));
	}
	
	@Override
	public void render(SpriteBatch batch) {}
	
	public void init(){
		super.init();
		pointLight = new PointLight(getGame().getRayHandler(), 500);
		pointLight.setColor(0, 1, 1, .8f);
		pointLight.setDistance(10);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		if(isInitialized() &&
				pointLight.getX() != getX() || pointLight.getY() != getY()){
			pointLight.setPosition(getX(), getY());
		}
	}
	
	public void store(){
		super.store();
		pointLight.remove(true);
	}
}
