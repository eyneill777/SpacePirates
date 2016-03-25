package gken.rustyice.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import box2dLight.PointLight;
import gken.rustyice.game.physics.PointPhysicsComponent;

public class SimpleLight extends Actor {
	private PointLight pointLight;
	private float lightRes = .25f;
	private float distance;
	
	public SimpleLight() {
		setPhysicsComponent(new PointPhysicsComponent(7, 7));
		distance = 10;
	}
	
	@Override
	public void render(SpriteBatch batch) {}
	
	public void init(){
		super.init();
		pointLight = new PointLight(getGame().getRayHandler(), getRayNum(distance, lightRes));
		pointLight.setColor(0, 1, 1, .8f);
		pointLight.setDistance(distance);
		System.out.println(getRayNum(distance, lightRes));
		//pointLight.setStaticLight(true);
	}
	
	public int getRayNum(float r, float space){
		return (int)((MathUtils.PI2 * r)/space);
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