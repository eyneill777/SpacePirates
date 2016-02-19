package Game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import resources.Resources;

public class GameWorld {
	private Camera camera;
	private Resources resources;
	private ArrayList<Actor> actors;
	private ArrayList<Actor> addList;
	
	public GameWorld(){
		camera = new Camera();
		camera.setWidth(50);
		camera.setHeight(50);
		
		actors = new ArrayList<>();
		addList = new ArrayList<>();
		addActor(new TestActor());

		//camera.setTarget(actors.get(0));
		//camera.setTracking(true);
	}
	
	public void addActor(Actor actor){
		addList.add(actor);
		actor.setGame(this);
	}
	
	public Actor getActor(int i){
		return actors.get(i);
	}
	
	public void setResources(Resources resources){
		this.resources = resources;
	}
	
	public Resources getResources(){
		return resources;
	}
	
	public void update(float delta){
		for(Actor actor: addList){
			actors.add(actor);
			actor.init();
		}
		addList.clear();
		
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
