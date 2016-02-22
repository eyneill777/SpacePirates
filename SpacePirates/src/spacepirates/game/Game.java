package spacepirates.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import spacepirates.game.level.Level;
import spacepirates.graphics.Camera;
import spacepirates.input.PlayerInput;
import spacepirates.resources.Resources;

public class Game implements ContactListener{
	private Camera camera;
	private Resources resources;
	private PlayerInput playerInput;
	private World world;
	private ArrayList<Actor> actors;
	private ArrayList<Actor> removeList;
	private ArrayList<Actor> addList;
	
	public Game(){
		world = new World(Vector2.Zero, true);
		world.setContactListener(this);
		
		camera = new Camera();
		camera.setWidth(10);
		camera.setHeight(10);
		
		Level level = new Level(5);
		
		actors = new ArrayList<>();
		addList = new ArrayList<>();
		removeList = new ArrayList<>();
		addActor(new Player());
		addActor(new TestActor());
	}
	
	public void setPlayerInput(PlayerInput playerInput){
		this.playerInput = playerInput;
	}
	
	public PlayerInput getPlayerInput(){
		return playerInput;
	}
	
	public void addActor(Actor actor){
		addList.add(actor);
		actor.setGame(this);
	}
	
	public void removeActor(Actor actor){
		removeList.add(actor);
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
		
		world.step(delta, 6, 2);
		
		for(Actor actor: actors){
			actor.update(delta);
		}
		
		for(Actor actor: removeList){
			actors.remove(actor);
			actor.remove();
		}
		removeList.clear();
		
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
	
	public World getWorld(){
		return world;
	}
	
	public void dispose(){
		world.dispose();
	}

	@Override
	public void beginContact(Contact contact) {
		Actor actorA = (Actor)contact.getFixtureA().getBody().getUserData();
		Actor actorB = (Actor)contact.getFixtureB().getBody().getUserData();
		
		actorA.beginCollision(contact.getFixtureA(), contact.getFixtureB(), contact);
		actorB.beginCollision(contact.getFixtureB(), contact.getFixtureA(), contact);
	}

	@Override
	public void endContact(Contact contact) {
		Actor actorA = (Actor)contact.getFixtureA().getBody().getUserData();
		Actor actorB = (Actor)contact.getFixtureB().getBody().getUserData();
		
		actorA.endCollision(contact.getFixtureA(), contact.getFixtureB(), contact);
		actorB.endCollision(contact.getFixtureB(), contact.getFixtureA(), contact);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}
	
}