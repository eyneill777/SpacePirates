package Game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameWorld {
	private Camera camera;
	private ArrayList<Actor> actors;
	
	public GameWorld(){
		camera = new Camera();
		camera.setWidth(50);
		camera.setHeight(50);
		
		actors = new ArrayList<>();
		actors.add(new TestActor());
		

		//camera.setTarget(actors.get(0));
		//camera.setTracking(true);
	}
	
	public void update(float delta){
		for(Actor actor: actors){
			actor.update(delta);
		}
		
		camera.update(delta);
	}
	
	public void render(SpriteBatch batch){
		for(Actor actor: actors){
			actor.render(batch);
		}
	}
	
	
	public Camera getCamrea(){
		return camera;
	}
	
}
